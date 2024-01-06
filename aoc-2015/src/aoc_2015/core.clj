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

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn day2 []
  (let [input (slurp "resources/2")
        split-by-x (fn [string]
                     (str/split string #"x"))
        get-dim (fn [line idx]
                  (Integer. (get (split-by-x line) idx)))
        get-second-smallest-side (fn [l w h]
                                   (let [sorted-dims (sort [l w h])]
                                     (nth sorted-dims 1)))
        part-one (fn []
                   (let [counter (atom 0)]
                     (doseq [line (str/split-lines input)]
                       (let [l (get-dim line 0)
                             w (get-dim line 1)
                             h (get-dim line 2)
                             area-1 (* l w)
                             area-2 (* w h)
                             area-3 (* h l)
                             smallest-side (min area-1 area-2 area-3)]
                         (reset! counter (+ @counter (* 2 area-1)))
                         (reset! counter (+ @counter (* 2 area-2)))
                         (reset! counter (+ @counter (* 2 area-3)))
                         (reset! counter (+ @counter smallest-side))))
                     @counter))
        part-two (fn []
                   (let [counter (atom 0)]
                     (doseq [line (str/split-lines input)]
                       (let [l (get-dim line 0)
                             w (get-dim line 1)
                             h (get-dim line 2)]
                         (reset! counter (+ @counter (* l w h)))
                         (reset! counter (+ @counter (* 2 (min l w h))))
                         (reset! counter (+ @counter (* 2 (get-second-smallest-side l w h))))))
                     @counter))]
    (println (part-one))
    (println (part-two))))

(defn day3 []
  (let [input (slurp "resources/3")
        part-one (fn []
                   (let [counter (atom 0)
                         x (atom 0)
                         y (atom 0)
                         visited (atom (set (str @x @y)))]
                     (doseq [c (seq input)]
                       (cond
                         (= c \>) (swap! x inc)
                         (= c \<) (swap! x dec)
                         (= c \^) (swap! y inc)
                         (= c \v) (swap! y dec))
                       (when (not (contains? @visited (str @x @y)))
                         (swap! counter inc)
                         (reset! visited (conj @visited (str @x @y)))))
                     @counter))
        part-two (fn []
                   (let [counter (atom 0)
                         santa-x (atom 0)
                         santa-y (atom 0)
                         robot-x (atom 0)
                         robot-y (atom 0)
                         visited (atom (set (str @santa-x @santa-y)))
                         idx (atom 0)]
                     (doseq [c (seq input)]
                       (if (= @idx 0)
                         (do (cond
                               (= c \>) (swap! santa-x inc)
                               (= c \<) (swap! santa-x dec)
                               (= c \^) (swap! santa-y inc)
                               (= c \v) (swap! santa-y dec))
                             (when (not (contains? @visited (str @santa-x @santa-y)))
                               (swap! counter inc)
                               (reset! visited (conj @visited (str @santa-x @santa-y))))
                             (swap! idx inc))
                         (do (cond
                               (= c \>) (swap! robot-x inc)
                               (= c \<) (swap! robot-x dec)
                               (= c \^) (swap! robot-y inc)
                               (= c \v) (swap! robot-y dec))
                             (when (not (contains? @visited (str @robot-x @robot-y)))
                               (swap! counter inc)
                               (reset! visited (conj @visited (str @robot-x @robot-y))))
                             (swap! idx dec))))
                     @counter))]
    (println (part-one))
    (println (part-two))))

(defn -main
  []
  (day3))
