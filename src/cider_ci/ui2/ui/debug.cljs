(ns cider-ci.ui2.ui.debug
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)]
    [reagent.ratom :as ratom :refer [reaction]]
    )
  (:require
    [cider-ci.utils.core :refer [presence]]
    [cider-ci.ui2.ui.state :as state]
    ))

(defn toggle-debug []
  (swap! state/client-state
         (fn [cs]
           (assoc cs :debug (not (:debug cs))))))

(defn page []
  [:div
   [:h1 "Debug"]
   [:p [:input {:style {:margin-left "1em"}
                :type "checkbox"
                :on-change toggle-debug
                :checked (:debug @state/client-state)
                }] " Debug state"]])
