(ns markov.core
  (:require [cemerick.piggieback] [cljs.repl.browser]))

  
(defn repl []
  (cemerick.piggieback/cljs-repl
   :repl-env (doto (cljs.repl.browser/repl-env :port 9000)
               cljs.repl/-setup)))

