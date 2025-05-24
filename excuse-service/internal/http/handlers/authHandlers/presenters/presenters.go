package presenters

import (
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"net/http"
	"strings"
)

type AuthenticatedResponse struct {
	Value string `json:"value"`
	Token string `json:"token"`
}
type RegisterRequest struct {
	Login    string `json:"login"`
	Password string `json:"password"`
	Username string `json:"username"`
}
type LoginRequest struct {
	Login    string `json:"login"`
	Password string `json:"password"`
}
type ChangePasswordRequest struct {
	OldPassword string `json:"oldPassword"`
	NewPassword string `json:"newPassword"`
}

type HandlerResponse struct {
	Value any `json:"value"`
}

type LogoutRequest struct {
	Token any `json:"value"`
}

func ExtractTokenFromHeader(r *http.Request) (string, error) {
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" {
		return "", errors.New("authorization header is missing")
	}

	parts := strings.Split(authHeader, " ")
	if len(parts) != 2 || parts[0] != "Bearer" {
		return "", errors.New("invalid Authorization header format")
	}

	// Возвращаем токен
	return parts[1], nil
}
func CreateRegisterRequest(r *http.Request) (*RegisterRequest, error) {
	var req RegisterRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		return nil, fmt.Errorf("error while decoding json: %v", err)
	}

	// Проверка на пустые поля
	if req.Login == "" {
		return nil, errors.New("login cannot be empty")
	}
	if req.Password == "" {
		return nil, errors.New("password cannot be empty")
	}
	if req.Username == "" {
		return nil, errors.New("username cannot be empty")
	}

	return &req, nil
}

func CreateLoginRequest(r *http.Request) (*LoginRequest, error) {
	var req LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		return nil, fmt.Errorf("error while decoding json: %v", err)
	}
	if req.Login == "" {
		return nil, errors.New("login cannot be empty")
	}
	if req.Password == "" {
		return nil, errors.New("password cannot be empty")
	}

	return &req, nil
}
func CreateChangePasswordRequest(r *http.Request) (*ChangePasswordRequest, error) {
	var req ChangePasswordRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		return nil, fmt.Errorf("error while decoding json: %v", err)
	}
	if req.OldPassword == "" {
		return nil, errors.New("login cannot be empty")
	}
	if req.NewPassword == "" {
		return nil, errors.New("password cannot be empty")
	}

	return &req, nil
}

func ProcessErrorAndResponse(w http.ResponseWriter, r any, err error) {
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if r != nil {
		w.Header().Set("Content-Type", "application/json")

		encoder := json.NewEncoder(w)
		encoder.SetIndent("", "    ")
		// Преобразуем в JSON строку для логов
		responseBytes, err := json.MarshalIndent(r, "", "    ")
		if err != nil {
			log.Printf("❌ Failed to marshal response for logging: %v", err)
		} else {
			log.Printf("✅ Sending response: %s", responseBytes)
		}
		if err := encoder.Encode(r); err != nil {
			log.Print("failed to send")
			http.Error(w, "InternalError", http.StatusInternalServerError)
		}
	}
}
