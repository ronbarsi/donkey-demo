(ns donkey-demo.asserions.demo
  (:require [erdos.assert :as ea]))

(try
  (ea/verify (= 1 (* 3 (/ 1 3))))
  (ea/verify (= 1.0 (* 3 (/ 1 3))))
  (catch Exception e
    (println (ex-message e))))
; (= (* 1 1.0) (* 3 (/ 1 3)))
;¦  ¦         ¦    ¦
;¦  1.0       1N   1/3
;false

(try
  (ea/verify (= {:a {:b {:c {:d 4}}}} {:a {:b {:c {:d 5}}}}))
  (catch Exception e
    (println (ex-message e))))
;(= {:a {:b {:c {:d 4}}}} {:a {:b {:c {:d 5}}}})
;¦
;false


(ea/examine (dotimes [i 5] (* i i)))
;(dotimes [i 5] (* i i))
;               ¦
;               0, 1, 4, 9, 16

(ea/examine (dotimes [i 5] (* i i) (str "*" i "*") (+ i i)))
;(dotimes [i 5] (* i i) (str "*" i "*") (+ i i))
;               ¦       ¦               ¦
;               ¦       ¦               0, 2, 4, 6, 8
;               ¦       "*0*", "*1*", "*2*", "*3*", "*4*"
;               0, 1, 4, 9, 16

(ea/examine (reduce + (map (fn [a] (* a a)) (range 8))))
;(reduce + (map (fn [a] (* a a)) (range 8)))
;¦         ¦            ¦        ¦
;140       (…)          ¦        (0 1 2 3 4 5 6 7)
;                       0, 1, 4, 9, 16, 25, 36, 49


(ea/examine (reduce + (map (fn [^:show a] (* a a)) (range 8))))
;(reduce + (map (fn [a] (* a a)) (range 8)))
;¦         ¦         ¦  ¦        ¦
;140       (…)       ¦  ¦        (0 1 2 3 4 5 6 7)
;                    ¦  0, 1, 4, 9, 16, 25, 36, 49
;                    0, 1, 2, 3, 4, 5, 6, 7

(ea/examine (reduce + (doall (map (fn [^:show a] (* a a)) (range 8)))))
;(reduce + (doall (map (fn [a] (* a a)) (range 8))))
;¦         ¦      ¦         ¦  ¦        ¦
;140       ¦      (…)       ¦  ¦        (0 1 2 3 4 5 6 7)
;          ¦                ¦  0, 1, 4, 9, 16, 25, 36, 49
;          ¦                0, 1, 2, 3, 4, 5, 6, 7
;          (0 1 4 9 16 25 36 49)
