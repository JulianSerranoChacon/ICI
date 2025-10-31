;; ============================================================
;; ===============   INKY RULES FILE   ========================
;; ============================================================

;; DEFINITION OF DATA TYPES 
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER))
)

(deftemplate MSPACMANclosestIntersection
	(slot index (type NUMBER))
)

;; NEW DATA TYPES

;; ADD DISTANCE FROM GHOST TO PACMAN 
(deftemplate BLINKYtoPacman (slot distanceTo (type FLOAT)))
(deftemplate INKYtoPacman (slot distanceTo (type FLOAT)))
(deftemplate PINKYtoPacman (slot distanceTo (type FLOAT)))
(deftemplate SUEtoPacman (slot distanceTo (type FLOAT)))

;; ADD DISTANCE FROM PACMAN TO GHOST 
(deftemplate PacmanToBLINKY (slot distanceTo (type FLOAT)))
(deftemplate PacmanToINKY (slot distanceTo (type FLOAT)))
(deftemplate PacmanToPINKY (slot distanceTo (type FLOAT)))
(deftemplate PacmanToSUE (slot distanceTo (type FLOAT)))

;; ADD DISTANCE FROM GHOST TO NEXT PACMAN INTERSECTION 
(deftemplate BLINKYToIntersection (slot distanceTo (type FLOAT)))
(deftemplate INKYToIntersection (slot distanceTo (type FLOAT)))
(deftemplate PINKYToIntersection (slot distanceTo (type FLOAT)))
(deftemplate SUEToIntersection (slot distanceTo (type FLOAT)))

;; DISTANCE FROM GHOST TO GHOST 
(deftemplate BLINKYToINKY (slot distanceTo (type FLOAT)))
(deftemplate BLINKYToPINKY (slot distanceTo (type FLOAT)))
(deftemplate BLINKYToSUE (slot distanceTo (type FLOAT)))
(deftemplate INKYToBLINKY (slot distanceTo (type FLOAT)))
(deftemplate INKYToPINKY (slot distanceTo (type FLOAT)))
(deftemplate INKYToSUE (slot distanceTo (type FLOAT)))
(deftemplate PINKYToBLINKY (slot distanceTo (type FLOAT)))
(deftemplate PINKYToINKY (slot distanceTo (type FLOAT)))
(deftemplate PINKYToSUE (slot distanceTo (type FLOAT)))
(deftemplate SUEToBLINKY (slot distanceTo (type FLOAT)))
(deftemplate SUEToINKY (slot distanceTo (type FLOAT)))
(deftemplate SUEToPINKY (slot distanceTo (type FLOAT)))

;; SHIELD GHOST 
(deftemplate BLINKYshieldGhost (slot ghost (type SYMBOL) (default NONE)))
(deftemplate INKYshieldGhost (slot ghost (type SYMBOL) (default NONE)))
(deftemplate PINKYshieldGhost (slot ghost (type SYMBOL) (default NONE)))
(deftemplate SUEshieldGhost (slot ghost (type SYMBOL) (default NONE)))

;; EDIBLE TIME GHOST 
(deftemplate BLINKYedible (slot edibleTime (type NUMBER)))
(deftemplate INKYedible (slot edibleTime (type NUMBER)))
(deftemplate PINKYedible (slot edibleTime (type NUMBER)))
(deftemplate SUEedible (slot edibleTime (type NUMBER)))

;; LAIR TIME GHOST 
(deftemplate BLINKYlair (slot lairTime (type NUMBER)))
(deftemplate INKYlair (slot lairTime (type NUMBER)))
(deftemplate PINKYlair (slot lairTime (type NUMBER)))
(deftemplate SUElair (slot lairTime (type NUMBER)))

;; DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id)
	(slot info (default ""))
	(slot priority (type NUMBER))
	(slot extraGhost (type SYMBOL) (default NONE))
	(slot intersection (type NUMBER) (default NONE))
)

;; ============================================================
;; ===============   INKY RULES   =============================
;; ============================================================

;; LAIR
(defrule INKYinlair
	(INKYlair (lairTime ?t))
	(test (> ?t 0))
	=>
	(assert (ACTION (id INKYRandom) (info "Random move") (priority 100)))
)

;; HUIDA
(defrule INKYpacmanFarAway
   (PacmanToINKY (distanceTo ?d))
   (INKYlair (lairTime ?t))
   (INKYedible (edibleTime ?e))
   (test (> ?e 0))
   (test (or (!= ?t 0) (> ?d (+ (/ ?e 2) 1))))
	=>
   (assert (ACTION (id INKYOrbit) (info "INKY far away and edible") (priority 21)))
)

(defrule INKYhayEscudero
	(INKYshieldGhost (ghost ?g))
	(INKYedible (edibleTime ?e))
	(test (> ?e 0))
	=>
	(assert (ACTION (id RunToEscuderoAction) (info "INKY going to escudero") (extraGhost ?g) (priority 20)))
)

(defrule INKYpacmanNear
   (PacmanToINKY (distanceTo ?d))
   (INKYlair (lairTime ?t))
   (INKYedible (edibleTime ?e))
   (test (> ?e 0))
   (test (or (== ?t 0) (< ?d 200)))
	=>
   (assert (ACTION (id INKYrunsOptimal) (info "INKY near and edible") (priority 19)))
)

(defrule INKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) 
	(test (<= ?d 30))
	=>
	(assert (ACTION (id "INKYstartRunning") (info "MSPacMan cerca PPill") (priority 18)))
)

;; PERSECUCION
(defrule INKYNearestToMsPacman
  (INKYtoPacman (distanceTo ?inkyDistance))
  (PINKYtoPacman (distanceTo ?pinkyDistance))
  (BLINKYtoPacman (distanceTo ?blinkyDistance))
  (SUEtoPacman (distanceTo ?sueDistance))
  (test (<= ?inkyDistance ?pinkyDistance))
  (test (<= ?inkyDistance ?blinkyDistance))
  (test (<= ?inkyDistance ?sueDistance))
	=>
	(assert (ACTION (id Hunter1) (info "Soy cazador1") (priority 15)))
)

(defrule INKYSecondNearestToMsPacman
  	(INKYtoPacman (distanceTo ?inkyDistance))
  	(PINKYtoPacman (distanceTo ?pinkyDistance))
  	(BLINKYtoPacman (distanceTo ?blinkyDistance))
  	(SUEtoPacman (distanceTo ?sueDistance))
	(test
		(or
			(and (> ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?blinkyDistance)
				(<= ?inkyDistance ?sueDistance)
				(bind ?closestGhost PINKY))
			(and (> ?inkyDistance ?blinkyDistance)
				(<= ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?sueDistance)
				(bind ?closestGhost BLINKY))
			(and (> ?inkyDistance ?sueDistance)
				(<= ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?blinkyDistance)
				(bind ?closestGhost SUE))
    	)
  	)
  =>
  (assert (ACTION (id Hunter2) (info "Soy Hunter2") (priority 14)))
)

(defrule INKYNearestToIntersection
  (MSPACMANclosestIntersection (index ?closestintersection))
  (INKYToIntersection (distanceTo ?inkyDistance))
  (PINKYToIntersection (distanceTo ?pinkyDistance))
  (BLINKYToIntersection (distanceTo ?blinkyDistance))
  (SUEToIntersection (distanceTo ?sueDistance))
  (test (<= ?inkyDistance ?pinkyDistance))
  (test (<= ?inkyDistance ?blinkyDistance))
  (test (<= ?inkyDistance ?sueDistance))
	=>
	(assert (ACTION (id JailerAction) (info "Soy Jailer") (priority 13)))
)

(defrule INKYBlinkingAndSafe
	(INKYtoPacman (distanceTo ?inkyDistance))
	(INKYedible (edibleTime ?e))
	(test (> ?inkyDistance 40))
	(test (< ?e 10))
	=>
	(assert (ACTION (id Hunter1) (info "Soy Hunter1") (priority 12)))
)
