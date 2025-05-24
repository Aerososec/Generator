package favoriteshandlers

import (
	"context"
	"errors"
	"fmt"
	"generator/excuse-service/internal/domain"
	authhandlers "generator/excuse-service/internal/http/handlers/authHandlers"
	"generator/excuse-service/internal/http/handlers/favoritesHandlers/presenters"
	"log"
	"net/http"
)

type userService interface {
	AddToFavorites(excuseID int, userID string) error
	UserFavorite(userID string) ([]domain.UserFavorite, error)
	DeleteFromFavorite(userID string, excuseID int) error
}

type FavoritesServer struct {
	service userService
}

func NewFavoritesServer(service userService) *FavoritesServer {
	return &FavoritesServer{service: service}
}

// GetUserID извлекает userId из контекста
func GetUserID(ctx context.Context) (string, error) {
	userID, ok := ctx.Value(authhandlers.UserIDKey).(string)
	if !ok {
		log.Print("userID not found in context")
		return "", errors.New("userID not found in context")
	}
	return userID, nil
}

// @Summary      Add Excuse to Favorites
// @Description  Add an excuse to the user's favorites.
// @Tags         Favorites
// @Accept       json
// @Produce      json
// @Param        Authorization header    string  true  "Authorization token in the format 'Bearer <token>'"
// @Param        excuseId    path      string  true  "excuseId"
// @Success      200  {object}  presenters.PostHandlerResponse
// @Failure      400  {string}  string  "Bad Request"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /favorites/add/{excuseId} [post]
func (s *FavoritesServer) PostToFavoriteHandler(w http.ResponseWriter, r *http.Request) {
	req, err := presenters.CreatePostToFavoriteRequest(r)
	if err != nil {
		log.Print(err.Error())
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	userID, err := GetUserID(r.Context())
	if err != nil {
		log.Print(err.Error())
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	err = s.service.AddToFavorites(req.ExcuseID, userID)
	presenters.ProcessErrorAndResponse(w, &presenters.PostHandlerResponse{Text: "u added excuse ", Id: req.ExcuseID}, err)
}

// @Summary      Get User Favorites
// @Description  Retrieve all favorite excuses for a specific user.
// @Tags         Favorites
// @Produce      json
// @Param        Authorization header    string  true  "Authorization token in the format 'Bearer <token>'"
// @Success      200       {object}  presenters.FavoritesHandlerResponse
// @Failure      400  {string}  string  "Bad Request"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /favorites [get]
func (s *FavoritesServer) GetUserFavorites(w http.ResponseWriter, r *http.Request) {
	userID, err := GetUserID(r.Context())
	if err != nil {
		log.Print(err.Error())
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	favorite, err := s.service.UserFavorite(userID)
	presenters.ProcessErrorAndResponse(w, &presenters.FavoritesHandlerResponse{Value: favorite}, err)
}

// @Summary      Delete Excuse from Favorites
// @Description  Remove an excuse from the user's favorites.
// @Tags         Favorites
// @Accept       json
// @Produce      json
// @Param        Authorization header    string  true  "Authorization token in the format 'Bearer <token>'"
// @Param        excuseId  path      string    true  "Excuse ID"
// @Success      200       {object}  presenters.DeleteHandlerResponse
// @Failure      400       {string}  string  "Bad Request"
// @Failure      500       {string}  string  "Internal Server Error"
// @Router       /favorites/{excuseId} [delete]
func (s *FavoritesServer) DeleteFromFavorite(w http.ResponseWriter, r *http.Request) {
	req, err := presenters.DeleteFromFavoriteRequest(r)
	if err != nil {
		log.Print(err.Error())
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	userID, err := GetUserID(r.Context())
	if err != nil {
		log.Print(err.Error())
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	err = s.service.DeleteFromFavorite(userID, req.ExcuseId)
	presenters.ProcessErrorAndResponse(w, &presenters.DeleteHandlerResponse{Text: fmt.Sprintf("u have deleted excuse with ID: %v", req.ExcuseId)}, err)
}
