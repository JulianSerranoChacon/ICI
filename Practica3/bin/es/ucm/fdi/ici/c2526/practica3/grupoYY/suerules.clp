;; ============================================================
;; ===============   SUE RULES FILE   =========================
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
;; ===============   SUE RULES   =============================
;; ============================================================

;; LAIR
(defrule SUEinlair
	(SUElair (lairTime ?t))
	(test (> ?t 0))
	=>
	(assert (ACTION (id SUERandom) (info "Random move") (priority 100)))
)

;; HUIDA
(defrule SUEpacmanFarAway
   (PacmanToSUE (distanceTo ?d))
   (SUElair (lairTime ?t))
   (SUEedible (edibleTime ?e))
   (test (> ?e 0))
   (test (or (!= ?t 0) (> ?d (+ (/ ?e 2) 1))))
	=>
   (assert (ACTION (id SUEOrbit) (info "SUE far away and edible") (priority 21)))
)

(defrule SUEhayEscudero
	(SUEshieldGhost (ghost ?g))
	(SUEedible (edibleTime ?e))
	(test (> ?e 0))
	=>
	(assert (ACTION (id RunToEscuderoAction) (info "SUE going to escudero") (extraGhost ?g) (priority 20)))
)

(defrule SUEpacmanNear
   (PacmanToSUE (distanceTo ?d))
   (SUElair (lairTime ?t))
   (SUEedible (edibleTime ?e))
   (test (> ?e 0))
   (test (or (== ?t 0) (< ?d 200)))
	=>
   (assert (ACTION (id SUERunsOptimal) (info "SUE near and edible") (priority 19)))
)

(defrule SUErunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) 
	(test (<= ?d 30))
	=>
	(assert (ACTION (id "SUEstartRunning") (info "MSPacMan cerca PPill") (priority 18)))
)

;; PERSECUCION
(defrule SUENearestToMsPacman
  (SUEtoPacman (distanceTo ?sueDistance))
  (PINKYtoPacman (distanceTo ?pinkyDistance))
  (INKYtoPacman (distanceTo ?inkyDistance))
  (BLINKYtoPacman (distanceTo ?blinkyDistance))
  (test (<= ?sueDistance ?pinkyDistance))
  (test (<= ?sueDistance ?inkyDistance))
  (test (<= ?sueDistance ?blinkyDistance))
	=>
	(assert (ACTION (id Hunter1) (info "Soy cazador1") (priority 15)))
)

(defrule SUESecondNearestToMsPacman
  	(SUEtoPacman (distanceTo ?sueDistance))
  	(PINKYtoPacman (distanceTo ?pinkyDistance))
  	(INKYtoPacman (distanceTo ?inkyDistance))
  	(BLINKYtoPacman (distanceTo ?blinkyDistance))
	(test
		(or
			(and (> ?sueDistance ?pinkyDistance)
				(<= ?sueDistance ?inkyDistance)
				(<= ?sueDistance ?blinkyDistance)
				(bind ?closestGhost PINKY))
			(and (> ?sueDistance ?inkyDistance)
				(<= ?sueDistance ?pinkyDistance)
				(<= ?sueDistance ?blinkyDistance)
				(bind ?closestGhost INKY))
			(and (> ?sueDistance ?blinkyDistance)
				(<= ?sueDistance ?pinkyDistance)
				(<= ?sueDistance ?inkyDistance)
				(bind ?closestGhost BLINKY))
    	)
  	)
  =>
  (assert (ACTION (id Hunter2) (info "Soy Hunter2") (priority 14)))
)

(defrule SUENearestToIntersection
  (MSPACMANclosestIntersection (index ?closestintersection))
  (SUEToIntersection (distanceTo ?sueDistance))
  (PINKYToIntersection (distanceTo ?pinkyDistance))
  (INKYToIntersection (distanceTo ?inkyDistance))
  (BLINKYToIntersection (distanceTo ?blinkyDistance))
  (test (<= ?sueDistance ?pinkyDistance))
  (test (<= ?sueDistance ?inkyDistance))
  (test (<= ?sueDistance ?blinkyDistance))
	=>
	(assert (ACTION (id JailerAction) (info "Soy Jailer") (priority 13)))
)

(defrule SUEBlinkingAndSafe
	(SUEtoPacman (distanceTo ?sueDistance))
	(SUEedible (edibleTime ?e))
	(test (> ?sueDistance 40))
	(test (< ?e 10))
	=>
	(assert (ACTION (id Hunter1) (info "Soy Hunter1") (priority 12)))
)
