package storage

import (
	"generator/excuse-service/internal/domain"
)

type DbStorage interface {
	GetAllCategories() ([]string, error)
	GetExcusesByName(categoryName string) ([]domain.Excuse, error)
	GetAllExcuses() ([]domain.Excuse, error)
	AddToFavorites(excuseID int, userID string) error
	UserFavorite(userID string) ([]domain.UserFavorite, error)
	DeleteFromFavorite(userID string, excuseID int) error
}
