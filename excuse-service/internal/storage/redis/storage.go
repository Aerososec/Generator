package redis

import (
	"context"
	"errors"
	"time"

	"github.com/redis/go-redis/v9"
)

type RedisStorage struct {
	client *redis.Client
}

func NewRadisConnectiron() (*RedisStorage, error) {
	client := redis.NewClient(&redis.Options{
		Addr:     "my-redis:6379", // Адрес сервера Redis
		Password: "",              // Пароль (если установлен)
		DB:       0,               // Используемая база данных
	})
	return &RedisStorage{client: client}, nil
}

func (r *RedisStorage) CreateSession(ctx context.Context, key string, value string, expiration time.Duration) error {
	if err := r.client.Set(ctx, key, value, expiration).Err(); err != nil {
		return errors.New("failde to create session")
	}
	return nil
}

func (r *RedisStorage) GetUserBySession(ctx context.Context, key string) (string, error) {
	val, err := r.client.Get(ctx, key).Result()
	if err != nil {
		return "", err
	}
	return val, nil
}
func (r *RedisStorage) DeleteSession(ctx context.Context, key string) error {
	if err := r.client.Del(ctx, key).Err(); err != nil {
		return errors.New("failde to delete session")
	}
	return nil
}
