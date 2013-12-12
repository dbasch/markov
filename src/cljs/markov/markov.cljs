(ns markov.core
  (:require clojure.string))

;; I/O with the DOM 

(defn input []
  (.-value (.getElementById js/document "text")))
  
(defn output [s]
  (set! (.-innerHTML (.getElementById js/document "output")) s))
  
;; Markov chain generation

(defn markov-data [text]
  (let [maps
        (for [line (clojure.string/split text #"\.")
              m (let [l (str line ".")
                      words
                      (cons "*START*" (clojure.string/split l #"\s+"))]
                  (for [p (partition 2 1 (remove empty? words))]
                    {(first p) [(second p)]}))]
          m)]
    (apply merge-with concat maps)))


(defn generate []
  (let [data (markov-data (input))]
    (loop [ws (data "*START*")
           acc []]
      (let [w (rand-nth ws)
            nws (data w)
            nacc (concat acc [w])]
        (if (= \. (last w))
          (output (clojure.string/join " " nacc))
          (recur nws nacc))))))