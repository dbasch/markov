(ns markov.core
  (:require [goog.dom :as dom]
            clojure.string))

(defn markov-data [line]
  (let [l (if (= \. (last line)) line (str line "."))
        words (filter #(re-find #"[a-z]" %) (.split l " "))
        ks (concat ["*START*"] (butlast words))
        pairs (partition 2 (interleave ks words))
        maps (for [p pairs] {(first p) [(second p)]})]
    (apply merge-with concat maps)))

(defn random-word [ws]
  (let [n (count ws)
        i (rand-int n)]
    (nth ws i)))

(defn markov []
  (let [text (.-value (.getElementById js/document "text"))]
    (let [sentences (clojure.string/split text #"\n")]
      (apply merge-with concat (map markov-data sentences)))))

(defn output [s]
  (set! (.-innerHTML (dom/getElement "output")) s))
  
(defn generate []
  (let [data (markov)]
    (loop [k (random-word (data "*START*"))
           acc [k]]
      (let [ws (data k)
            w (random-word ws)
            nacc (concat acc [w])]
        (if (= \. (last w))
          (output (clojure.string/join " " nacc))
          (recur w nacc))))))