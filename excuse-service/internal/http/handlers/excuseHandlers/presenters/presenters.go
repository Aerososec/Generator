package presenters

import (
	"encoding/json"
	"errors"
	"net/http"

	"github.com/go-chi/chi"
)

type GetHandlerRequest struct {
	CategoryName string `json:"CategoryName"`
}
type GetHandlerResponse struct {
	Value any `json:"value"`
}

func GetExcuseByCategoryRequest(r *http.Request) (*GetHandlerRequest, error) {
	categoryName := chi.URLParam(r, "categoryName")
	if categoryName == "" {
		return nil, errors.New("categoryName is empty")
	}
	return &GetHandlerRequest{CategoryName: categoryName}, nil
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

		if err := encoder.Encode(r); err != nil {
			http.Error(w, "InternalError", http.StatusInternalServerError)
		}
	}
}
