(ns aoc-2015.core
  (:gen-class)
  (:require
   [clojure.string :as str]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn day1 []
  (let [input (slurp "resources/1")
        part-one (fn []
                   (let [counter (atom 0)]
                     (doseq [c (seq input)]
                       (cond
                         (= c \() (swap! counter inc)
                         (= c \)) (swap! counter dec)))
                     @counter))
        part-two (fn []
                   (let [counter (atom 0)
                         pos (atom 0)
                         charnumber (atom 0)]
                     (doseq [c (seq input)]
                       (cond
                         (= c \() (swap! counter inc)
                         (= c \)) (swap! counter dec))
                       (when (and (= @counter -1) (= @pos 0))
                         (reset! pos (+ @charnumber 1)))
                       (swap! charnumber inc))
                     @pos))]
    (println (part-one))
    (println (part-two))))

(defn day2 []
  (let [input (slurp "resources/2")
        split-by-x (fn [string]
                     (str/split string #"x"))
        get-l (fn [line]
                (Integer. (get (split-by-x line) 0)))
        get-w (fn [line]
                (Integer. (get (split-by-x line) 1)))
        get-h (fn [line]
                (Integer. (get (split-by-x line) 2)))
        get-smallest-side (fn [area-1 area-2 area-3]
                            (min area-1 area-2 area-3))
        part-one (fn []
                   (let [counter (atom 0)]
                     (doseq [line (str/split-lines input)]
                       (let [l (get-l line)
                             w (get-w line)
                             h (get-h line)
                             area-1 (* 2 (* l w))
                             area-2 (* 2 (* w h))
                             area-3 (* 2 (* h l))
                             smallest-side (get-smallest-side area-1 area-2 area-3)]
                         (reset! counter (+ @counter area-1))
                         (reset! counter (+ @counter area-2))
                         (reset! counter (+ @counter area-3))
                         (reset! counter (+ @counter smallest-side))))
                     @counter))
        part-two (fn [] ())]
    (println (part-one))
    (println (part-two))))

(defn -main
  []
  (day2))
