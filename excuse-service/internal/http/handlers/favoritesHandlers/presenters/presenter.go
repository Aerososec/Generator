package presenters

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"strconv"

	"github.com/go-chi/chi"
)

type PostHandlerResponse struct {
	Text string `json:"text"`
	Id   int    `json:"id"`
}
type GetHandlerRequestUserId struct {
	Id string `json:"id"`
}
type DeleteHandlerRequest struct {
	ExcuseId int `json:"excuseId"`
}
type FavoritesHandlerResponse struct {
	Value any `json:"value"`
}

type DeleteHandlerResponse struct {
	Text string `json:"text"`
}
type FavoriteHandlerRequest struct {
	ExcuseID int `json:"excuseID"`
}

func CreatePostToFavoriteRequest(r *http.Request) (*FavoriteHandlerRequest, error) {
	excuseIdStr := chi.URLParam(r, "excuseId")
	if excuseIdStr == "" {
		return nil, errors.New("excuseId is empty")
	}

	// Преобразуем строку в int
	excuseId, err := strconv.Atoi(excuseIdStr)
	if err != nil {
		return nil, errors.New("excuseId must be a valid integer")
	}

	return &FavoriteHandlerRequest{ExcuseID: excuseId}, nil
}
func GetUserFavoritesRequest(r *http.Request) (*GetHandlerRequestUserId, error) {
	userIdStr := chi.URLParam(r, "userId")
	if userIdStr == "" {
		return nil, errors.New("userId is empty")
	}
	return &GetHandlerRequestUserId{Id: userIdStr}, nil
}
func DeleteFromFavoriteRequest(r *http.Request) (*DeleteHandlerRequest, error) {
	excuseIdStr := chi.URLParam(r, "excuseId")

	if excuseIdStr == "" {
		return nil, errors.New("userId or excuseId is empty")
	}
	excuseId, err := strconv.Atoi(excuseIdStr)
	if err != nil {
		return nil, errors.New("excuseId must be a valid integer")
	}

	return &DeleteHandlerRequest{ExcuseId: excuseId}, nil

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
		responseBytes, err := json.MarshalIndent(r, "", "    ")
		if err != nil {
			log.Printf("❌ Failed to marshal response for logging: %v", err)
		} else {
			log.Printf("✅ Sending response: %s", responseBytes)
		}

		if err := encoder.Encode(r); err != nil {
			http.Error(w, "InternalError", http.StatusInternalServerError)
		}
	}
}
