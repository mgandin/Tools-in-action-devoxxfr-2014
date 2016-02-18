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