(ns arena-code-challenge.core-test
  (:require [clojure.test :refer :all]
            [arena-code-challenge.core :as sut]))

(deftest test-adding-list-to-artist-new-map
  (testing "Tests that a list id is added to the set of list ids for an artist when the artist-map-is empty."
    (let [artist-map {}
          artist     "Social Distortion"
          list-id    "list-1"]
      (is (= (sut/add-artist-to-list list-id artist-map artist)
             {"Social Distortion" #{list-id}})))))

(deftest test-adding-list-to-artist-new-artist
  (testing "Tests that adding a new artist to a non-empty artist-map."
    (let [artist-map {"Social Distortion" #{"list-1"}}
          artist     "NOFX"
          list-id    "list-1"]
      (is (= (sut/add-artist-to-list list-id artist-map artist)
             {"Social Distortion" #{list-id}
              "NOFX" #{list-id}})))))

(deftest test-adding-list-to-artist-new-list
  (testing "Tests that adding a new list to an artist in a non-empty artist-map."
    (let [artist-map {"Social Distortion" #{"list-1"}
                      "NOFX"              #{"list-1"}}
          artist     "NOFX"
          list-id    "list-2"]
      (is (= (sut/add-artist-to-list list-id artist-map artist)
             {"Social Distortion" #{"list-1"}
              "NOFX" #{"list-1" list-id}})))))
