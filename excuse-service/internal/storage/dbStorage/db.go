package dbStorage

import (
	"errors"
	"generator/excuse-service/internal/domain"
	"log"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type DbStorage struct {
	db *gorm.DB
}

func NewDatabaseStorage() *DbStorage {
	// dsn := fmt.Sprintf(
	// 	"host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
	// 	os.Getenv("DB_HOST"),
	// 	os.Getenv("DB_USER"),
	// 	os.Getenv("DB_PASSWORD"),
	// 	os.Getenv("DB_NAME"),
	// 	os.Getenv("DB_PORT"),
	// )
	dsn := "host=db user=mitia password=12345 dbname=excuse_generator port=5432 sslmode=disable"

	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatalf("failed to connect database: %v", err)
	}

	db.AutoMigrate(&domain.User{}, &domain.Category{}, &domain.Excuse{}, &domain.Session{}, &domain.UserFavorite{})
	return &DbStorage{db: db}
}

func (s *DbStorage) GetAllCategories() ([]string, error) {
	var categoryNames []string
	if err := s.db.Model(&domain.Category{}).Pluck("category_name", &categoryNames).Error; err != nil {
		return nil, err
	}
	return categoryNames, nil
}

func (s *DbStorage) GetAllExcuses() ([]domain.Excuse, error) {
	var excuses []domain.Excuse
	if err := s.db.Preload("Category").Find(&excuses).Error; err != nil {
		return nil, err
	}
	return excuses, nil
}

func (s *DbStorage) GetExcusesByName(categoryName string) ([]domain.Excuse, error) {
	var category domain.Category
	if err := s.db.Preload("Excuses").Preload("Excuses.Category").Where("category_name = ?", categoryName).First(&category).Error; err != nil {
		return nil, err
	}
	return category.Excuses, nil
}

func (s *DbStorage) AddToFavorites(excuseID int, userID string) error {
	var user domain.User
	if err := s.db.Where("id = ?", userID).First(&user).Error; err != nil {
		return errors.New("user not found")
	}

	var existing domain.UserFavorite
	var excuse domain.Excuse

	if err := s.db.Where("id = ?", excuseID).First(&excuse).Error; err != nil {
		return errors.New("there is no such excuse")
	}

	if err := s.db.Where("user_id = ? AND excuse_id = ?", userID, excuseID).First(&existing).Error; err == nil {
		return errors.New("this excuse have already been added")
	}

	favorite := domain.UserFavorite{
		UserID:   userID,
		ExcuseID: excuseID,
	}

	if err := s.db.Create(&favorite).Error; err != nil {
		return err
	}

	return nil
}

func (s *DbStorage) UserFavorite(userID string) ([]domain.UserFavorite, error) {
	var user domain.User
	if err := s.db.Where("id = ?", userID).First(&user).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, errors.New("user not found")
		}
		return nil, err
	}

	var favorites []domain.UserFavorite
	if err := s.db.Where("user_id = ?", userID).
		Preload("Excuse").
		Preload("Excuse.Category").
		Find(&favorites).Error; err != nil {
		return nil, err
	}
	return favorites, nil
}

func (s *DbStorage) DeleteFromFavorite(userID string, excuseID int) error {
	result := s.db.Where("user_id = ? AND excuse_id = ?", userID, excuseID).Delete(&domain.UserFavorite{})
	if result.Error != nil {
		return result.Error
	}

	if result.RowsAffected == 0 {
		return errors.New("no record found to delete")
	}

	return nil
}
func (s *DbStorage) GetUserByLogin(login string) (*domain.User, error) {
	var user domain.User
	if err := s.db.Where("login = ?", login).First(&user).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, errors.New("user not found")
		}
		return nil, err
	}
	return &user, nil
}
func (s *DbStorage) GetUserById(id string) (*domain.User, error) {
	var user domain.User
	if err := s.db.Where("id = ?", id).First(&user).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, errors.New("user not found")
		}
		return nil, err
	}
	return &user, nil
}
func (s *DbStorage) UpdateUserPassword(userID string, hashedPassword string) error {
	result := s.db.Model(&domain.User{}).
		Where("id = ?", userID).
		Update("password", hashedPassword)

	if result.Error != nil {
		return result.Error
	}
	if result.RowsAffected == 0 {
		return errors.New("no password found to delete")
	}
	return nil
}

func (s *DbStorage) CreateNewUser(user domain.User) error {
	var existingUser domain.User
	if err := s.db.Where("login = ?", user.Login).First(&existingUser).Error; err == nil {
		return errors.New("user with this login already exists")
	}
	if err := s.db.Create(&user).Error; err != nil {
		return err
	}

	return nil
}

func (s *DbStorage) CreateSession(userId string, sessionId string) error {
	session := domain.Session{
		SessionID: sessionId,
		UserID:    userId,
	}
	if err := s.db.Create(&session).Error; err != nil {
		return err
	}
	return nil
}

func (s *DbStorage) GetUserBySession(sessionId string) (string, error) {
	var session domain.Session
	if err := s.db.Where("session_id = ?", sessionId).First(&session).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return "", errors.New("user not found")
		}
		return "", err
	}
	return session.UserID, nil

}

func (s *DbStorage) DeleteSession(sessionId string) error {
	var session domain.Session
	if err := s.db.Where("session_id = ?", sessionId).First(&session).Delete(&domain.Session{}).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return errors.New("there is no session to delete")
		}
		return err
	}
	return nil

}
