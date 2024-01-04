(ns aoc-2015.core
  (:gen-class)
  (:require
   [clojure.string :as str]))

(defn day1 []
  (def input (slurp "resources/1"))
  (defn part-one []
    (def counter (atom 0))
    (doseq [c (seq input)]
      (cond
        (= c \() (swap! counter inc)
        (= c \)) (swap! counter dec)))
    @counter)
  (defn part-two []
    (def counter (atom 0))
    (def pos (atom 0))
    (def charnumber (atom 0))
    (doseq [c (seq input)]
      (cond
        (= c \() (swap! counter inc)
        (= c \)) (swap! counter dec))
      (if (and (= @counter -1) (= @pos 0))
        (reset! pos (+ @charnumber 1)))
      (swap! charnumber inc))
    @pos)
  (println (part-one))
  (println (part-two)))

(defn day2 []
  (def input (slurp "resources/2"))
  (defn split-by-x [string]
    (str/split string #"x"))
  (defn get-l [line]
    (Integer. (get (split-by-x line) 0)))
  (defn get-w [line]
    (Integer. (get (split-by-x line) 1)))
  (defn get-h [line]
    (Integer. (get (split-by-x line) 2)))
  (defn get-smallest-side [x y z]
    (min x y z))
  (defn part-one []
    (def counter (atom 0))
    (doseq [line (str/split-lines input)]
      (def l (get-l line))
      (def w (get-w line))
      (def h (get-h line))
      (def area-1 (* 2 (* l w)))
      (def area-2 (* 2 (* w h)))
      (def area-3 (* 2 (* h l)))
      (def smallest-side (get-smallest-side area-1 area-2 area-3))
      (reset! counter (+ @counter area-1))
      (reset! counter (+ @counter area-2))
      (reset! counter (+ @counter area-3))
      (reset! counter (+ @counter smallest-side)))
    @counter)
  (defn part-two [] ())
  (println (part-one))
  (println (part-two)))

(defn -main
  [& _]
  (day2))
