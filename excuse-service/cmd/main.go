package main

import (
	"flag"
	"log"

	authhandlers "generator/excuse-service/internal/http/handlers/authHandlers"
	excusehandlers "generator/excuse-service/internal/http/handlers/excuseHandlers"
	favoritehandlers "generator/excuse-service/internal/http/handlers/favoritesHandlers"

	_ "generator/excuse-service/docs"
	"generator/excuse-service/internal/http"
	pkghttp "generator/excuse-service/internal/init"
	"generator/excuse-service/internal/storage/dbStorage"
	redisStorage "generator/excuse-service/internal/storage/redis"
	authService "generator/excuse-service/internal/usecases/authService"
	userservice "generator/excuse-service/internal/usecases/userService"

	"github.com/go-chi/chi"
	httpSwagger "github.com/swaggo/http-swagger"
)

// @title My API
// @version 1.0
// @description This is a sample server.

// @host localhost:8080
// @BasePath /

func main() {
	db := dbStorage.NewDatabaseStorage()
	ExcuseService := userservice.NewObject(db)

	redis, err := redisStorage.NewRadisConnectiron()
	if err != nil {
		log.Fatalf("Failed to start redis: %v", err)
	}
	AuthService := authService.NewObject(db, redis)

	authServer := authhandlers.NewAuthServer(AuthService)

	excuseServer := excusehandlers.NewexcuseServer(ExcuseService)
	favoriteServer := favoritehandlers.NewFavoritesServer(ExcuseService)

	addr := flag.String("addr", ":8080", "address for http server")

	r := chi.NewRouter()
	r.Get("/swagger/*", httpSwagger.WrapHandler)

	http.WithObjectHandlers(r, authServer, excuseServer, favoriteServer)

	log.Printf("Starting server on %s", *addr)
	if err := pkghttp.CreateAndRunServer(r, *addr); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
