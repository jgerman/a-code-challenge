(ns arena-code-challenge.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set])
  (:gen-class))

(def base-file "Artist_lists_small.txt")

(defn read-file [name]
  (->> name
       io/resource
       slurp
       str/split-lines
       (remove empty?)))

(defn list->artists [l]
  (str/split l #","))

(defn add-artist-to-list
  "Given an artist map, a list id that the artist appears on, and an artist;
  update the artist map such that the list-id is added to the set of lists that
  artist appears on."
  [list-id artist-map artist]
  (update artist-map artist (comp set conj) list-id))

(defn update-artist-map-from-list
  [artist-map list-contents]
  (reduce (partial add-artist-to-list (java.util.UUID/randomUUID))
          artist-map
          (list->artists list-contents)))

(defn create-artist-map-from-file
  [file-name]
  (let [file-contents (read-file file-name)]
    (reduce update-artist-map-from-list {} file-contents)))

(defn filter-interesting-artists
  "Takes an artist map and a threshold and returns only those artists that are
  intersting, i.e. could meet the criteria."
  [artist-map threshold]
  (into {} (filter #(-> %
                        val
                        count
                        (>= threshold))
                   artist-map)))

(defn compare-artists
  "artist-1 and artist2 are:
  [artist-name list-ids].

  Return a 3-tuple:
  [artist-1 artist-2 number-of-shared-lists]."
  [artist-1 artist-2]
  [(first artist-1)
   (first artist-2)
   (count (set/intersection (second artist-1)
                            (second artist-2)))])

(defn map-an-artist
  "Maps an artist tuple:
   [artist-name list-ids]
  across a list of the same using the compare artist function."
  [artist artist-list]
  (map (partial compare-artists artist) artist-list))

(defn find-shared-lists
  "Given a collection of artist tuples, recursively find how many lists they have
  in common with the (remaining) artists."
  [artist-map]
  (loop [m artist-map
         pairs '()]
    (if (empty? m)
      pairs
      (recur (rest m)
             (doall (concat pairs
                            (map-an-artist (first m) (rest m))))))))

(defn filter-pairs-by-threshold [pairs threshold]
  (filter #(<= threshold (nth % 2)) pairs))

(defn run-search [file-name threshold]
  (-> file-name
      create-artist-map-from-file
      (filter-interesting-artists threshold)
      find-shared-lists
      (filter-pairs-by-threshold threshold)))

(defn format-output [artist-pairing]
  (apply format "%s,%s" artist-pairing))

(defn -main
  [& args]
  (let [results (run-search (first args)
                            (read-string (second args)))]
    (doseq [artist-pairing results]
      (println (format-output artist-pairing)))))
