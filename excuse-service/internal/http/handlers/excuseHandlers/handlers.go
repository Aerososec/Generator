package excusehandlers

import (
	"generator/excuse-service/internal/domain"
	"generator/excuse-service/internal/http/handlers/excuseHandlers/presenters"
	"math/rand"
	"net/http"
)

type userService interface {
	GetAllCategories() ([]string, error)
	GetExcusesByName(categoryName string) ([]domain.Excuse, error)
	GetAllExcuses() ([]domain.Excuse, error)
}

func NewexcuseServer(service userService) *ExcuseServer {
	return &ExcuseServer{service: service}
}

type ExcuseServer struct {
	service userService
}

// @Summary      Get Random Excuse
// @Description  Retrieve a random excuse from the database.
// @Tags         Excuses
// @Produce      json
// @Success      200  {object}  presenters.GetHandlerResponse
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /excuses/random [get]
func (s *ExcuseServer) GetRandomExcuseHandler(w http.ResponseWriter, r *http.Request) {
	excuses, err := s.service.GetAllExcuses()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	randomExcuse := excuses[rand.Intn(len(excuses))]
	presenters.ProcessErrorAndResponse(w, &presenters.GetHandlerResponse{Value: randomExcuse}, err)
}

// @Summary      Get Excuses by Category
// @Description  Retrieve excuses by category name.
// @Tags         Excuses
// @Accept       json
// @Produce      json
// @Param        categoryName    path      string  true  "CategoryName"
// @Success      200  {object}  presenters.GetHandlerResponse
// @Failure      400  {string}  string  "Bad Request"
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /excuses/{categoryName} [get]
func (s *ExcuseServer) GetExcuseByCategoryNameHandler(w http.ResponseWriter, r *http.Request) {
	req, err := presenters.GetExcuseByCategoryRequest(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	categories, err := s.service.GetExcusesByName(req.CategoryName)
	presenters.ProcessErrorAndResponse(w, &presenters.GetHandlerResponse{Value: categories}, err)
}

// @Summary      Get All Categories
// @Description  Retrieve all category names.
// @Tags         Categories
// @Produce      json
// @Success      200  {object}  presenters.GetHandlerResponse
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /categories [get]
func (s *ExcuseServer) GetAllCategoriesHandler(w http.ResponseWriter, r *http.Request) {
	categories, err := s.service.GetAllCategories()
	presenters.ProcessErrorAndResponse(w, &presenters.GetHandlerResponse{Value: categories}, err)
}

// @Summary      Get All Excuses
// @Description  Retrieve all excuses from the database.
// @Tags         Excuses
// @Produce      json
// @Success      200  {object}  presenters.GetHandlerResponse
// @Failure      500  {string}  string  "Internal Server Error"
// @Router       /excuses [get]
func (s *ExcuseServer) GetAllExcusesHandler(w http.ResponseWriter, r *http.Request) {
	excuses, err := s.service.GetAllExcuses()
	presenters.ProcessErrorAndResponse(w, &presenters.GetHandlerResponse{Value: excuses}, err)
}
