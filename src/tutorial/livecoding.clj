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