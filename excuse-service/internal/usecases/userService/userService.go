package userservice

import (
	"generator/excuse-service/internal/domain"
	"generator/excuse-service/internal/storage"
)

type Object struct {
	storage storage.DbStorage
}

func NewObject(service storage.DbStorage) *Object {
	return &Object{storage: service}
}

func (s *Object) GetAllCategories() ([]string, error) {
	return s.storage.GetAllCategories()
}

func (s *Object) GetAllExcuses() ([]domain.Excuse, error) {
	return s.storage.GetAllExcuses()
}

func (s *Object) AddToFavorites(excuseID int, userID string) error {
	return s.storage.AddToFavorites(excuseID, userID)
}

func (s *Object) DeleteFromFavorite(userID string, excuseID int) error {
	return s.storage.DeleteFromFavorite(userID, excuseID)
}

func (s *Object) UserFavorite(userID string) ([]domain.UserFavorite, error) {
	return s.storage.UserFavorite(userID)
}

func (s *Object) GetExcusesByName(categoryName string) ([]domain.Excuse, error) {
	return s.storage.GetExcusesByName(categoryName)
}
