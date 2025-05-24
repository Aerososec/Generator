package authhandlers

import (
	"context"
	"generator/excuse-service/internal/http/handlers/authHandlers/presenters"
	"log"
	"net/http"
)

type userService interface {
	Login(login, password string) (string, error)
	Register(login, password, username string) error
	Auth(token string) (string, error)
	DeleteSession(sessionId string) error
	NewPassword(id, oldPassword, newPassword string) error
}

type AuthServer struct {
	service userService
}

func NewAuthServer(service userService) *AuthServer {
	return &AuthServer{service: service}
}

// @Summary      Register User
// @Description  Register a new user with a username and password.
// @Tags         Auth
// @Accept       json
// @Produce      json
// @Param        body  body      presenters.RegisterRequest  true  "User credentials"
// @Success      200  {object}  presenters.HandlerResponse
// @Failure      400  {string}  string  "Bad Request"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /auth/register [post]
func (s *AuthServer) RegisterHandler(w http.ResponseWriter, r *http.Request) {
	req, err := presenters.CreateRegisterRequest(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	err = s.service.Register(req.Login, req.Password, req.Username)
	presenters.ProcessErrorAndResponse(w, &presenters.HandlerResponse{Value: "Registration successful"}, err)
}

// @Summary      Login User
// @Description  Login a user and return a session token.
// @Tags         Auth
// @Accept       json
// @Produce      json
// @Param        body  body      presenters.LoginRequest  true  "User credentials"
// @Success      200  {object}  presenters.AuthenticatedResponse
// @Failure      400  {string}  string  "Bad Request"
// @Failure      401  {string}  string  "Unauthorized"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /auth/login [post]
func (s *AuthServer) LoginHandler(w http.ResponseWriter, r *http.Request) {
	req, err := presenters.CreateLoginRequest(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	sessionToken, err := s.service.Login(req.Login, req.Password)
	presenters.ProcessErrorAndResponse(w, &presenters.AuthenticatedResponse{Value: "log in was succesful", Token: sessionToken}, err)
}

// @Summary      Logout User
// @Description  Logout a user by invalidating their session token.
// @Tags         Auth
// @Accept       json
// @Produce      json
// @Param        Authorization header    string  true  "Authorization token in the format 'Bearer <token>'"
// @Success      200  {object}  presenters.HandlerResponse
// @Failure      401  {string}  string  "Unauthorized"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /auth/logout [post]
func (s *AuthServer) LogoutHandler(w http.ResponseWriter, r *http.Request) {
	token, err := presenters.ExtractTokenFromHeader(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}
	err = s.service.DeleteSession(token)
	if err != nil {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}
	presenters.ProcessErrorAndResponse(w, &presenters.HandlerResponse{Value: "Logout successful"}, err)
}

// @Summary      Change Password
// @Description  Change the user's password. Requires old and new password.
// @Tags         Auth
// @Accept       json
// @Produce      json
// @Param        body  body      presenters.ChangePasswordRequest  true  "Old and new password"
// @Param        Authorization header    string  true  "Bearer token"
// @Success      200  {object}  presenters.HandlerResponse
// @Failure      400  {string}  string  "Bad Request"
// @Failure      401  {string}  string  "Unauthorized"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /auth/changePassword [post]
func (s *AuthServer) NewPasswordHandler(w http.ResponseWriter, r *http.Request) {
	userID, ok := r.Context().Value(UserIDKey).(string)
	if !ok {
		http.Error(w, "failed to userId in context", http.StatusUnauthorized)
	}
	req, err := presenters.CreateChangePasswordRequest(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	if err = s.service.NewPassword(userID, req.OldPassword, req.NewPassword); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	presenters.ProcessErrorAndResponse(w, &presenters.HandlerResponse{Value: "password  was successfully changed"}, err)

}

type AuthResponse struct {
	UserID string `json:"userId"`
}
type AuthRequet struct {
	Token string `json:"token"`
}

type contextKey string

const UserIDKey contextKey = "userID"

// @Summary      Middleware for Authentication
// @Description  Middleware to validate user authentication token.
// @Tags         Middleware
// @Failure      400  {string}  string  "Bad Request"
// @Failure      401  {string}  string  "Unauthorized"
func (s *AuthServer) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		token, err := presenters.ExtractTokenFromHeader(r)
		if err != nil {
			log.Print(err.Error())
			http.Error(w, err.Error(), http.StatusUnauthorized)
			return
		}
		userId, err := s.service.Auth(token)
		if err != nil {
			log.Print(err.Error())
			http.Error(w, err.Error(), http.StatusUnauthorized)
			return
		}
		ctx := context.WithValue(r.Context(), UserIDKey, userId)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}
