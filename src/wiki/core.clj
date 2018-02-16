(ns wiki.core
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn query
  [q & {:keys [l] :or {l "en"}}]
  (def url (format
             "https://%s.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=%s"
             l q))
  (println url)
  (def response (client/get url))
  (if (= 200 (:status response))
    (json/read-str (:body response) :key-fn keyword)
    (throw (Exception. "RequestError"))
    )
)

(defn find-nested
  [m k]
  (->> (tree-seq map? vals m)
       (filter map?)
       (some k)))

(defn summary
  [json-map]
  (def summary (find-nested json-map :extract))
  (if summary
    (summary)
    (throw (Exception. "ContentNotFound"))
    )
  )
