(ns reacl-c.test-util.dom-predicates-test
  (:require [reacl-c.test-util.dom-predicates :as dp]
            [cljs.test :refer (deftest is testing) :include-macros true]))

(deftest test-1
  (let [n (doto (js/document.createElement "div")
            (.appendChild (js/document.createTextNode "foo")))]
    (is (dp/text-content= n "foo"))
    (is (not (dp/disabled? n)))))
