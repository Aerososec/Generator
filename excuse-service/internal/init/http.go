package init

import (
	"net/http"

	"github.com/go-chi/chi"
)

func CreateAndRunServer(r chi.Router, addr string) error {

	httpServer := &http.Server{
		Addr:    addr,
		Handler: r,
	}

	return httpServer.ListenAndServe()
}
