package http

import (
	authhandlers "generator/excuse-service/internal/http/handlers/authHandlers"
	excusehandlers "generator/excuse-service/internal/http/handlers/excuseHandlers"
	favoriteshandlers "generator/excuse-service/internal/http/handlers/favoritesHandlers"

	"github.com/go-chi/chi"
)

func WithObjectHandlers(r chi.Router,
	authServer *authhandlers.AuthServer,
	excuseServer *excusehandlers.ExcuseServer,
	favoritesServer *favoriteshandlers.FavoritesServer) {
	r.Route("/categories", func(r chi.Router) {
		r.Get("/", excuseServer.GetAllCategoriesHandler)
	})
	r.Route("/favorites", func(r chi.Router) {
		r.Use(authServer.AuthMiddleware)
		r.Get("/", favoritesServer.GetUserFavorites)
		r.Post("/add/{excuseId}", favoritesServer.PostToFavoriteHandler)
		r.Delete("/{excuseId}", favoritesServer.DeleteFromFavorite)
	})
	r.Route("/excuses", func(r chi.Router) {
		r.Get("/", excuseServer.GetAllExcusesHandler)
		r.Get("/random", excuseServer.GetRandomExcuseHandler)
		r.Get("/{categoryName}", excuseServer.GetExcuseByCategoryNameHandler)
	})
	r.Route("/auth", func(r chi.Router) {
		r.Post("/register", authServer.RegisterHandler)
		r.Post("/login", authServer.LoginHandler)
		r.Post("/logout", authServer.LogoutHandler)

		r.Group(func(r chi.Router) {
			r.Use(authServer.AuthMiddleware)
			r.Post("/changePassword", authServer.NewPasswordHandler)
		})
	})

}
