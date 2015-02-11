(ns metabase.api.card
  (:require [compojure.core :refer [GET DELETE]]
            [korma.core :refer :all]
            [metabase.api.common :refer :all]
            [metabase.db :refer :all]
            (metabase.models [hydrate :refer [hydrate]]
                             [card :refer :all]
                             [card-favorite :refer :all])))

(defendpoint GET "/" [org f] ; TODO - need to do something with the `f` param
  (-> (sel :many Card :organization_id org (order :name :ASC))
      (hydrate :creator)))

(defendpoint GET "/:id" [id]
  (or-404-> (sel :one Card :id id)
    (hydrate :can_read :can_write)))

(defendpoint DELETE "/:id" [id]
  ;; TODO - permissions check (!)
  (del Card :id id))

(defendpoint GET "/:id/favorite" [id]
  {:favorite (boolean (some->> *current-user-id*
                               (sel :one CardFavorite :card_id id :owner_id)))})

(define-routes)
