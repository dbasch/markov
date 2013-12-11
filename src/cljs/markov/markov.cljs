(ns markov.core
  (:require clojure.string))

;; I/O with the DOM 

(defn input []
  (.-value (.getElementById js/document "text")))
  
(defn output [s]
  (set! (.-innerHTML (.getElementById js/document "output")) s))
  
;; Markov chain generation

(defn markov-data [line]
  (let [l (if (= \. (last line)) line (str line "."))
        words (cons "*START*" (clojure.string/split l #"\s"))
        maps (for [p (partition 2 1 words)] {(first p) [(second p)]})]
    (apply merge-with concat maps)))

(defn text-to-markov [text]
  (let [sentences (clojure.string/split text #"\n")]
    (apply merge-with concat (map markov-data sentences))))

(defn generate []
  (let [data (text-to-markov (input))]
    (loop [ws (data "*START*")
           acc []]
      (let [w (rand-nth ws)
            nws (data w)
            nacc (concat acc [w])]
        (if (= \. (last w))
          (output (clojure.string/join " " nacc))
          (recur nws nacc))))))