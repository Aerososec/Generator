package authservice

import (
	"context"
	"crypto/rand"
	"encoding/base64"
	"errors"
	"fmt"
	"generator/excuse-service/internal/domain"
	"time"

	"github.com/google/uuid"
	"golang.org/x/crypto/bcrypt"
)

func hash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		return "", errors.New("failed hashing")
	}
	return string(bytes), nil
}

func checkPassword(password, hash string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}

func generateToken(lenght int) (string, error) {
	bytes := make([]byte, lenght)
	if _, err := rand.Read(bytes); err != nil {
		return "", fmt.Errorf("error while decoding token: %v", err)
	}
	return base64.URLEncoding.EncodeToString(bytes), nil
}

type DbStorage interface {
	CreateNewUser(user domain.User) error
	GetUserByLogin(login string) (*domain.User, error)
	CreateSession(userId string, sessionId string) error
	GetUserBySession(sessionId string) (string, error)
	DeleteSession(sessionId string) error
	GetUserById(id string) (*domain.User, error)
	UpdateUserPassword(userID string, hashedPassword string) error
}
type Redis interface {
	CreateSession(ctx context.Context, key string, value string, expiration time.Duration) error
	GetUserBySession(ctx context.Context, key string) (string, error)
	DeleteSession(ctx context.Context, key string) error
}

type Object struct {
	pgStorage    DbStorage
	redisStorage Redis
}

func NewObject(service DbStorage, redis Redis) *Object {
	return &Object{pgStorage: service, redisStorage: redis}
}

func (s *Object) Register(login, password, usename string) error {
	if len(login) < 4 || len(password) < 4 {
		return errors.New("too short login or password")
	}
	hashedPassword, err := hash(password)
	if err != nil {
		return err
	}

	user := domain.User{
		ID:       uuid.New().String(),
		UserName: usename,
		Login:    login,
		Password: hashedPassword,
	}

	if err := s.pgStorage.CreateNewUser(user); err != nil {
		return err
	}

	return nil
}

func (s *Object) Login(login, password string) (string, error) {
	user, err := s.pgStorage.GetUserByLogin(login)
	if err != nil || !checkPassword(password, user.Password) {
		return "", errors.New("invalid password or login")
	}
	sessionToken, err := generateToken(32)
	if err != nil {
		return "", err
	}
	//s.pgStorage.CreateSession(user.ID, sessionToken)
	s.redisStorage.CreateSession(context.Background(), sessionToken, user.ID, 5*time.Minute)
	return sessionToken, nil
}

func (s *Object) NewPassword(id, oldPassword, newPassword string) error {
	user, err := s.pgStorage.GetUserById(id)
	if err != nil {
		return err
	}

	if !checkPassword(oldPassword, user.Password) {
		return errors.New("invalid current password")
	}

	if len(newPassword) < 4 {
		return errors.New("new password is too short (min 4 symbols)")
	}

	hashedPassword, err := hash(newPassword)
	if err != nil {
		return fmt.Errorf("failed to hash new password: %w", err)
	}

	if err := s.pgStorage.UpdateUserPassword(id, hashedPassword); err != nil {
		return fmt.Errorf("failed to update password: %w", err)
	}

	return nil
}

func (s *Object) Auth(token string) (string, error) {
	userId, err := s.redisStorage.GetUserBySession(context.Background(), token)
	if err != nil {
		return "", err
	}
	return userId, err
}

func (s *Object) DeleteSession(token string) error {
	return s.redisStorage.DeleteSession(context.Background(), token)
}
