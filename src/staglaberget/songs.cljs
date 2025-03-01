(ns staglaberget.songs
  (:require ["abcjs" :as abcjs]
            [reagent.dom.client :as rdc]
            [clojure.edn :as edn]
            [reagent.core :as r]))

(defonce state (r/atom nil))

(def render-abc (get (js->clj abcjs) "renderAbc"))

(def instruments
  {"guitar"        {:instrument "guitar"
                    :label      "Guitar (%T)"
                    :tuning     ["E,", "A,", "D", "G", "B", "e"] ;; E2 A2 D3 G3 B3 E4
                    :capo       0}
   "guitar-dadgad" {:instrument "guitar"
                    :label      "Guitar (%T)"
                    :tuning     ["D,", "A,", "D", "G", "A", "d"]
                    :capo       0}
   "mandolin"      {:instrument "mandolin"
                    :label      "Mandolin (%T)"
                    :tuning     ["G,", "D", "A", "e"]
                    :capo       0}
   "mandola"       {:instrument "mandolin"
                    :label      "Mandola (%T)"
                    :tuning     ["C,", "G,", "D", "A"]
                    :capo       0}})

(defn ->tablature [s]
  (when s
    (get instruments s)))

(defonce root-container
  (rdc/create-root (js/document.getElementById "app")))

(def wagon-wheel-abc
  "
X: 1
T: Whagon Wheel
M: 4/4
L: 1/8
K: A
P: Verse
| \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB] | \"F#m\" [FAC]2 [FAC][FAC] [FAC]2 [FAC][FAC]| \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] |
w: Headed  down _ south to the land of the pines _ I'm thumbin my way _ in to | North Car o line
|  \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB]  | \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] | [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] |
w: Starin' up the road _ and pray to god _ I see | head- lights | _ _ _ _ I
|  \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB] | \"F#m\" [FAC]2 [FAC][FAC] [FAC]2 [FAC][FAC]| \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] |
w:  made it down the coast in | sev en teen hours, | pickin' me a bou- quet of | dog wood  flow'rs and I'm a
| \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB] | \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] | [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] |
w: hopin' for Ra- leigh, I can | see my ba- by to _ | night. | _ _ _ _ So,
P: Chorus
| \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB] | \"F#m\" [FAC]2 [FAC][FAC] [FAC]2 [FAC][FAC]|
w:rock me ma- ma like a | wag on wheel | rock me ma- ma an- y
| \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] | \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB]  | \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] |
w: way you feel. | Hey, | _ _ _  ma- ma | rock me. |
| \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] | \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB] | \"F#m\" [FAC]2 [FAC][FAC] [FAC]2 [FAC][FAC]|
w: _ | Rock me, ma- ma like the | wind and the rain, | rock me ma- ma like a |
| \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] | \"A\" [ACE]2 [ACE][ACE] [ACE]2 [ACE][ACE] | \"E\" [EGB]2 [EGB][EGB] [EGB]2 [EGB][EGB]  | \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] |
w: south- bound train | Hey, | _ _ _ ma- ma | rock me.
|1 \"D\" [DFA]2 [DFA][DFA] [DFA]2 [DFA][DFA] :||
|2 \"D\" afed cA B2 | \"A\" [ACE]2  ||
W: 1.
W: Runnin' from the cold up in New England
W: I was born to be a fiddler in an old-time string band
W: My baby plays the guitar, I pick the banjo now
W: Oh, the north-country winter keep a- gettin' me down.
W: Lost my money playin' poker so I had to leave the town,
W: But I ain't turnin' back to livin' that old lite no more.
W:
W: 2.
W: Walkin' due south of Roanoke,
W: I caught a trucker out of Philly, had a nice long toke
W: But he's a headed west from the Cumberland Gap to Johnson City, Tennessee.
W: And I gotta get a move on before the sun.
W: I hear my caby callin' my name and I know that she's the only one,
W: And if I die in Raleigh, at least I will die free.
")

(defn wagon-wheel-score [tab]
  (render-abc
    "wagon-wheel-score"
    wagon-wheel-abc
    (clj->js
      (cond-> {:responsive "resize"}
        tab (assoc :tablature [tab])))))

(defn scores [state']
  (wagon-wheel-score (->tablature state')))

(defn ui []
  (r/create-class
    {:component-did-mount  #(scores @state)
     :component-did-update #(scores @state)

     ;; name your component for inclusion in error messages
     :display-name "scores"

     ;; note the keyword for this method
     :reagent-render
     (fn []
       [:div
        [:p "Välj ett instrument om du vill ha tabbar till noterna. "]
        [:select {:value (prn-str (or @state "Select instrument"))
                  :on-change (fn [evt]
                               (let [value (-> evt .-target .-value edn/read-string)]
                                 (reset! state (if (= value "Välj instrument")
                                                 nil
                                                 value))))}
         (for [t (concat ["Select instrument"] (keys instruments))]
           ^{:key t}
           [:option {:value (prn-str t)} t])]
        [:br]
        [:div {:id "wagon-wheel-score"}]
        [:br]
        [:hr]])}))

(defn mount-ui
  []
  (rdc/render root-container [ui]))

(defn ^:dev/after-load init []
  (mount-ui))

(mount-ui)
