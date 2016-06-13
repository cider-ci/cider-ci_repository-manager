; Copyright © 2013 - 2016 Dr. Thomas Schank <Thomas.Schank@AlgoCon.ch>
; Copyright © 2013 - 2016 Dr. Thomas Schank <Thomas.Schank@AlgoCon.ch>
; Licensed under the terms of the GNU Affero General Public License v3.
; See the "LICENSE.txt" file provided with this software.

(ns cider-ci.repository.project-configuration.task-generation
  (:require
    [cider-ci.repository.git.repositories :as git.repositories]
    [cider-ci.repository.sql.repository :as sql.repository]

    [cider-ci.utils.core :refer [deep-merge]]
    [cider-ci.utils.rdbms :as rdbms]

    [clojure.walk :refer [keywordize-keys]]

    [clojure.tools.logging :as logging]
    [logbug.catcher :as catcher]
    [logbug.debug :as debug]
    ))



;##############################################################################

(defn- file-name-to-task [file-name]
  [file-name {:environment_variables
              {:CIDER_CI_TASK_FILE file-name}}])

(defn generate-tasks [git-ref-id context]
  (if-let [generate-spec (:generate_tasks context)]
    (let [repository (sql.repository/resolve git-ref-id)
          include-regex (-> generate-spec :include_match)
          exclude-regex (-> generate-spec :exclude_match)
          file-list (git.repositories/ls-tree
                      repository git-ref-id include-regex exclude-regex)
          generated-tasks (->> file-list
                               (map file-name-to-task)
                               (into {})
                               clojure.walk/keywordize-keys)
          tasks (if-let [existing-tasks (:tasks context)]
                  (cond (map? existing-tasks) (deep-merge
                                                generated-tasks existing-tasks)
                        :else (throw (IllegalStateException.
                                       (str "tasks must be a map"
                                            " to be merged with generated-tasks"))))
                  generated-tasks)]
      (-> context
          (assoc :tasks tasks)
          (dissoc :generate_tasks)))
    context))

;### Debug ####################################################################
;(logging-config/set-logger! :level :debug)
;(logging-config/set-logger! :level :info)
;(debug/debug-ns *ns*)
