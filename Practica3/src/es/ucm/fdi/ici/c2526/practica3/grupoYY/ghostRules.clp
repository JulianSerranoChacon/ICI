;; DEFINITION OF DATA TYPES ;;
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )

(deftemplate MSPACMANclosestIntersection
	(slot index (type NUMBER)))
 
;;NEW DATA TYPES

;;  ADD DISTANCE FROM GHOST TO PACMAN ;;

(deftemplate BLINKYtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float
   
(deftemplate INKYtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float   
  
(deftemplate PINKYtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float
	
(deftemplate SUEtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float  

;;  ADD DISTANCE FROM PACMAN TO GHOST ;;

(deftemplate PacmanToBLINKY
	(slot distanceTo (type FLOAT)))   
   
(deftemplate PacmanToINKY
	(slot distanceTo (type FLOAT)))   
  
(deftemplate PacmanToPINKY
	(slot distanceTo (type FLOAT)))   
	
(deftemplate PacmanToSUE
	(slot distanceTo (type FLOAT)))    

	;;  ADD DISTANCE FROM GHOST TO NEXT PACMAN INTERSECTION ;;

(deftemplate BLINKYToIntersection
	(slot distanceTo (type FLOAT)))   
   
(deftemplate INKYToIntersection
	(slot distanceTo (type FLOAT)))   
  
(deftemplate PINKYToIntersection
	(slot distanceTo (type FLOAT)))   
	
(deftemplate SUEToIntersection
	(slot distanceTo (type FLOAT)))    


;; DISTANCE FROM GHOST TO GHOST ;;

;;BLINKY
(deftemplate BLINKYToINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate BLINKYToPINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate BLINKYToSUE
	(slot distanceTo (type FLOAT)))  

;;INKY	
(deftemplate INKYToBLINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate INKYToPINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate INKYToSUE
	(slot distanceTo (type FLOAT)))  

;;PINKY
(deftemplate PINKYToBLINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate PINKYToINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate PINKYToSUE
	(slot distanceTo (type FLOAT)))  

;;SUE
(deftemplate SUEToBLINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate SUEToINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate SUEToPINKY
	(slot distanceTo (type FLOAT)))  

;; SHIELD GHOST ;;

(deftemplate shieldGhost
	(slot ghost (type SYMBOL))
	(slot covers (type SYMBOL))
) 
	
;; EDIBLE TIME GHOST ;;

(deftemplate BLINKYedible
	(slot edibleTime (type NUMBER))) 

(deftemplate INKYedible
	(slot edibleTime (type NUMBER))) 
	
(deftemplate PINKYedible
	(slot edibleTime (type NUMBER))) 
	
(deftemplate SUEedible
	(slot edibleTime (type NUMBER))) 
	
;; LAIR TIME GHOST ;;

(deftemplate BLINKYlair
	(slot lairTime (type NUMBER))) 

(deftemplate INKYlair
	(slot lairTime (type NUMBER))) 
	
(deftemplate PINKYlair
	(slot lairTime (type NUMBER))) 
	
(deftemplate SUElair
	(slot lairTime (type NUMBER))) 
 
;; DEFINITION OF THE ACTION FACT (ALSO A DATA_TYPE lol) --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; Basic action
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot extraGhost (type SYMBOL) (default: NONE)) ; Extra slot for any action that requires a target ghost
	(slot intersection (type NUMBER) (default: NONE)) ; Extra slot for any action that requires a target intersection
) 

;; -------------------------------------------------------------------------------------------;;

;; RULES OF ALL GHOSTS --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; LAIR ;;
(defrule BLINKYinlair
	(BLINKYlair (lairTime ?t)
	(test (> ?t 0))
	=>
	(assert (ACTION (id BLINKYRandom) (info "Random move")  (priority 100) ))
)	

;; HUIDA ;;
(defrule BLINKYpacmanFarAway
   (PacmanToBLINKY (distanceTo ?d))
   (BLINKYlair (lairTime ?t))
   (BLINKYedible (edibleTime ?e))
   (test (> ?e 0))
   (test (or (!= ?t 0) (> ?d (+ (/ ?e 2) 1))))  ;; far away if distance > (edibleTime/2 + 1)
	=>
   (assert
      (ACTION 
         (id BLINKYOrbit)
         (info "BLINKY far away and edible")
         (priority 22) 		;;Reassign priority
      )
   )
)

(defrule BLINKYhayEscudero
	(shieldGhost (ghost ?g) (covers BLINKY))
	(test (> ?e 0))
	=>
   	(assert
    	(ACTION 
        	(id RunToEscuderoAction)
         	(info "BLINKY going to escudero")
         	(extraGhost ?g)
         	(priority 20) 		
      	)
	)
)

(defrule BLINKYpacmanNear
   (PacmanToBLINKY (distanceTo ?d))
   (BLINKYlair (lairTime ?t))
   (BLINKYedible (edibleTime ?e))
   (test (> ?e 0))
   (test (or (== ?t 0) (< ?d 200)))  ;; near if distance < 200
=>
   (assert
      (ACTION 
         (id BLINKYrunsOptimal)
         (info "BLINKY near and edible")
         (priority 19) 		
      )
   )
)

;;(defrule BLINKYhaPasadoEscudero prioridad 21

(defrule BLINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION (id "BLINKYstatrRunning") (info "MSPacMan cerca PPill") (priority 18) 
		)
	)
)

;; PERSECUCION ;;
(defrule BLINKYediblesNearPacman
	(PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  	(INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  	(SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
	(BLINKYedible (edibleTime ?blinkyEdible))
	(INKYedible (edibleTime ?INKYEdible))
	(SUEedible (edibleTime ?SUEEdible))
	(PINKYedible (edibleTime ?PINKYEdible))

	(and
		(or
			(and (PINKYedible (edibleTime ?PINKYEdible&:(> ?PINKYEdible 0)))
				(PINKYtoPacman (distanceTo ?pinkyDistance&:(< ?pinkyDistance 100))))
			(and (INKYedible (edibleTime ?INKYEdible&:(> ?INKYEdible 0)))
				(INKYtoPacman (distanceTo ?inkyDistance&:(< ?inkyDistance 100))))
			(and (SUEedible (edibleTime ?SUEEdible&:(> ?SUEEdible 0)))
				(SUEtoPacman (distanceTo ?sueDistance&:(< ?sueDistance 100))))
    )
	=>
	(assert (ACTION (id "BLINKYrunToTheEdible") (info "me vuelvo escudero") (extraGhost (priority 17)
)

(defrule BLINKYNearestToMsPacman ; HAY QUE CAMBIAR SEGUN EL FANTASMA QUE SEAS
  (BLINKYtoPacman (distanceTo ?blinkyDistance)) ; Hecho para la distancia de Blinky
  (PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  (INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  (SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
  (test (<= ?blinkyDistance ?pinkyDistance))
  (test (<= ?blinkyDistance ?inkyDistance))
  (test (<= ?blinkyDistance ?sueDistance))
	=> 
	(assert (ACTION (id BLINKYHunter1) (info "Soy cazador1")  (priority 15) ))
)

(defrule BLINKYSecondNearestToMsPacman
  (BLINKYtoPacman (distanceTo ?blinkyDistance)) ; Hecho para la distancia de Blinky
  (PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  (INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  (SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
  (test
    (or
      (and (> ?blinkyDistance ?pinkyDistance)
           (<= ?blinkyDistance ?inkyDistance)
           (<= ?blinkyDistance ?sueDistance)
           (bind ?closestGhost PINKY))
      (and (> ?blinkyDistance ?inkyDistance)
           (<= ?blinkyDistance ?pinkyDistance)
           (<= ?blinkyDistance ?sueDistance)
           (bind ?closestGhost INKY))
      (and (> ?blinkyDistance ?sueDistance)
           (<= ?blinkyDistance ?pinkyDistance)
           (<= ?blinkyDistance ?inkyDistance)
           (bind ?closestGhost SUE))
    )
  )
  =>
  (assert (ACTION (id BLINKYHunter2) (info "Soy Hunter2") (extraGhost ?closestGhost) (priority 14)))
)

(defrule BLINKYNearestToIntersection
  (MSPACMANclosestIntersection (index ?closestintersection))
  (BLINKYToIntersection (distanceTo ?blinkyDistance)) ; Hecho para la distancia de Blinky
  (PINKYToIntersection (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  (INKYToIntersection (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  (SUEToIntersection (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
  (test (<= ?blinkyDistance ?pinkyDistance))
  (test (<= ?blinkyDistance ?inkyDistance))
  (test (<= ?blinkyDistance ?sueDistance))
	=> 
	(assert (ACTION (id BLINKYJailer) (info "Soy Jailer") (intersection ?closestintersection)  (priority 13) ))
)

(defrule BLINKYrandom ; HAY QUE CAMBIAR SEGUN EL FANTASMA QUE SEAS
	(assert (ACTION (id BLINKYRandom) (info "Random move")  (priority 12) ))
)	