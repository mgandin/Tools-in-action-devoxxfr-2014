(use 'overtone.live)

(use 'overtone.inst.drum)
(use 'overtone.inst.piano)
(use 'overtone.inst.synth)


(def kickkick (sample (freesound-path 2086)))
(def snaresnare (sample (freesound-path 26903)))
(def clapclap (sample (freesound-path 48310)))

(definst drone [freq 220]
  (sin-osc freq))

(drone)
(drone 440)
(drone 660)

(stop)

(definst modulation [freq 220]
  (bpf(saw freq) (mouse-x 4 5000 EXP) (mouse-y 1 0.1 LIN)))

(modulation)

(stop)

(defsynth synthe [freq 220]
  (out 0 (bpf(saw freq) (mouse-x 4 5000 EXP) (mouse-y 1 0.1 LIN)))
  (out 1 (bpf(sin-osc freq) (mouse-x 4 5000 EXP) (mouse-y 1 0.1 LIN))))

(synthe)
(stop)

(on-event [:midi :note-on]
          (fn[event]
            (let [note (:note event)]
              (overpad (* 2 note))))
          :piano-handler)

(remove-event-handler :piano-handler)

(def c-major [(note :C4) (note :D4) (note :E4) (note :F4) (note :G4) (note :A4) (note :B4) (note :C5)])

(doseq [note c-major]
  (piano note)
  (Thread/sleep 200))

(defonce again? (atom true))

(future
  (while @again?
    (kickkick)
    (Thread/sleep 200)
    (snaresnare)
    (Thread/sleep 200)))

(reset! again? false)

(defn hello [current period]
  (println "Hello Devoxx" current)
  (let [new (+ current period)]
    (apply-at new #'hello [new period])))

(hello (now) 200)

(stop)

(defn looper [current period sound]
  (at current (sound))
  (let [new (+ current period)]
    (apply-at new #'looper [new period sound])))

(looper (now) 200 kickkick)
(looper (now) 1600 clapclap)
(stop)

(defn sequencer [current period sound pattern]
  (at current (when (= 1 (first pattern))
                (sound)))
  (let [new (+ current period)]
    (apply-at new #'sequencer [new period sound (rest pattern)])))

(sequencer (now) 200 kickkick (cycle[1 1 0 1 0 1 0 0]))
(sequencer (now) 200 clapclap (cycle[1 0 0 0 0 0 0 1]))
(sequencer (now) 200 snaresnare (cycle[0 0 1 0 0 1 0 0]))

(stop)