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
        words (filter #(re-find #"[a-z]" %) (clojure.string/split l #"\s"))
        ks (concat ["*START*"] (butlast words))
        pairs (partition 2 (interleave ks words))
        maps (for [p pairs] {(first p) [(second p)]})]
    (apply merge-with concat maps)))

(defn random-word [ws]
  (let [n (count ws)
        i (rand-int n)]
    (nth ws i)))

(defn markov [text]
  (let [sentences (clojure.string/split text #"\n")]
    (apply merge-with concat (map markov-data sentences))))

(defn generate []
  (let [data (markov (input))]
    (loop [k (random-word (data "*START*"))
           acc [k]]
      (let [ws (data k)
            w (random-word ws)
            nacc (concat acc [w])]
        (if (= \. (last w))
          (output (clojure.string/join " " nacc))
          (recur w nacc))))))