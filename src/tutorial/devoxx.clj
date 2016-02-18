(use 'overtone.live)
(use 'overtone.inst.synth)
(use 'overtone.inst.drum)
(use 'overtone.synth.sts)
(use 'overtone.inst.piano)

; Some free samples from freesound.org
(def kickkick (sample (freesound-path 2086)))
(def clapclap (sample (freesound-path 48310)))
(def snaresnare (sample (freesound-path 26903)))
(def water (sample (freesound-path 50623)))
(def bassbass (sample (freesound-path 128306)))
(def voice (sample (freesound-path 147881)))
(def ghost (sample (freesound-path 180023)))
(def scary-piano (sample (freesound-path 166748)))


(def patterns {kickkick [1 1 0 1 0 1 0 0]
           bassbass [1 0 0 1 0 0 1 0]
           clapclap   [1 0 0 0 0 0 0 1]})


(def live-pats (atom patterns))

(defn live-sequencer
  ([curr-t sep-t live-patterns] (live-sequencer curr-t sep-t live-patterns 0))
  ([curr-t sep-t live-patterns beat]
     (doseq [[sound pattern] @live-patterns
             :when (= 1 (nth pattern (mod beat (count pattern))))]
       (at curr-t (sound)))
     (let [new-t (+ curr-t sep-t)]
       (apply-by new-t #'live-sequencer [new-t sep-t live-patterns (inc beat)]))))


(live-sequencer (+ 200 (now)) 200 live-pats)

(swap! live-pats assoc ghost [0 0 0 0 0 0 0 0])
(swap! live-pats assoc kickkick [0 0 0 0 0 0 0 0])
(swap! live-pats assoc water [0 0 0 0 0 0 0 0])
(swap! live-pats assoc bassbass [0 0 0 0 0 0 0 0])
(swap! live-pats assoc voice [0 0 0 0 0 0 0 0])
(swap! live-pats assoc clapclap   [0 0 0 0 0 0 0 0])

(swap! live-pats assoc snaresnare   [0 0 0 0 0 0 0 0])
(swap! live-pats assoc overpad   [0 0 0 0 0 0 0 0])

(defsynth wobble-bass [amp 1 note 52 wobble 1 detune 1.01 wob-lo 200 wob-hi 20000 pan 0]
  (let [saws          (mix (saw [note (* note detune)]))
        wob-freq      (lin-exp (lf-saw wobble) -1 1 wob-lo wob-hi)
        wob-freq      (lag wob-freq 0.05)
        filtered-saws (lpf saws wob-freq)
        normalized    (normalizer filtered-saws)
        amplified     (* amp normalized)]
    (out 0 (pan2 amplified pan))))

(wobble-bass)

(kill 1193)

(on-event [:midi :note-on]
          (fn[e]
          (let[note (:note e)]
            (overpad (* note 2))))
          :pad-handler)

(remove-event-handler :pad-handler)
(stop)

