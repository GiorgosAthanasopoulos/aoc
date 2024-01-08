(ns aoc-2015.core
  (:gen-class)
  (:require
   [clojure.string :as str]))
(import 'java.security.MessageDigest
        'java.math.BigInteger)

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

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
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
                       (if (= (mod @idx 2) 0)
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
                             (swap! idx inc))))
                     @counter))]
    (prn (part-one))
    (prn (part-two))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn day4 []
  (let [input (str/trim-newline (slurp "resources/4"))
        counter (atom 0)
        key (atom (str input @counter))
        md5 (fn [s]
              (let [algorithm (MessageDigest/getInstance "MD5")
                    size (* 2 (.getDigestLength algorithm))
                    raw (.digest algorithm (.getBytes s))
                    sig (.toString (BigInteger. 1 raw) 16)
                    padding (apply str (repeat (- size (count sig)) "0"))]
                (str padding sig)))
        part-one (fn []
                   (while (not (= (subs (md5 @key) 0 5) "00000"))
                     (swap! counter inc)
                     (reset! key (str input @counter)))
                   @counter)
        part-two (fn []
                   (while (not (= (subs (md5 @key) 0 6) "000000"))
                     (swap! counter inc)
                     (reset! key (str input @counter)))
                   @counter)]
    (prn (part-one))
    (prn (part-two))))

(defn day5 []
  (let [input (slurp "resources/5")
        contains-substring (fn [main-str sub-str]
                             (not= nil (clojure.string/index-of main-str sub-str)))
        three-vowels (fn [string]
                       (let [counter (atom 0)]
                         (doseq [c (seq string)]
                           (when (contains-substring "aeiou" (str c))
                             (swap! counter inc)))
                         (> @counter 2)))
        double-letter (fn [string]
                        (not (nil? (re-matches #".*([a-zA-Z])\1.*" string))))
        not-contain-illegal (fn [string]
                              (and (not (contains-substring string "ab"))
                                   (not (contains-substring string "cd"))
                                   (not (contains-substring string "pq"))
                                   (not (contains-substring string "xy"))))
        is-string-nice (fn [string]
                         (and (three-vowels string)
                              (double-letter string)
                              (not-contain-illegal string)))
        part-one (fn []
                   (let [counter (atom 0)]
                     (doseq [string (str/split-lines input)]
                       (when (is-string-nice string)
                         (swap! counter inc)))
                     @counter))
        contains-pair (fn [string]
                        ())
        contains-repeating-letter (fn [string]
                                    ())
        is-string-nice-2 (fn [string]
                           ())
        part-two (fn []
                   (let [counter (atom 0)]
                     (doseq [string (str/split-lines input)]
                       (when (is-string-nice-2 string)
                         (swap! counter inc)))
                     @counter))]
    (prn (part-one))
    (prn (part-two))))

(defn -main
  []
  (day5))
