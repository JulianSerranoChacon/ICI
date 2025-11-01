;; DEFINITION OF DATA TYPES ;;

(deftemplate MSPACMAN
	(slot distanceToBlinky 			(type integer) (default -1))
	(slot distanceToPinky 			(type integer) (default -1))
	(slot distanceToInky 			(type integer) (default -1))
	(slot distanceToSue 			(type integer) (default -1))
	(slot closestIntersection 		(type integer) (default -1))
	(slot distanceToClosestPPill 	(type integer) (default -1))	
)

(deftemplate BLINKY
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToPinky 			(type integer)  (default -1))
	(slot distanceToInky 			(type integer)  (default -1))
	(slot distanceToSue 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate INKY
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToBlinky 			(type integer)  (default -1))
	(slot distanceToPinky 			(type integer)  (default -1))
	(slot distanceToSue 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate PINKY
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToBlinky 			(type integer)  (default -1))
	(slot distanceToInky 			(type integer)  (default -1))
	(slot distanceToSue 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate SUE
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToBlinky 			(type integer)  (default -1))
	(slot distanceToPinky 			(type integer)  (default -1))
	(slot distanceToInky 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

;; DEFINITION OF THE ACTION FACT (ALSO A DATA_TYPE lol) --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; Basic action
(deftemplate ACTION
	; MANDATORY SLOTS ;
	(slot id			(type SYMBOL)			  ) 
	(slot info 			(type STRING) (default "")) 
	; OPTIONAL SLOTS ; 
	(slot extraGhost 	(type SYMBOL) (default NONE)) ; Extra slot for any action that requires a target ghost
	(slot intersection 	(type NUMBER) (default NONE)) ; Extra slot for any action that requires a target intersection
	(slot priority 		(type NUMBER) (default NONE))
) 

;; -------------------------------------------------------------------------------------------;;

;; RULES OF ALL GHOSTS ;;

;; DEDUCED INFORMATION ;;
;; LAIR ;;
(defrule INKYinlair
	(INKY (lairTime ?t))
	(test (> ?t 0))
	=>
	(assert 
		(ACTION 
			(id INKYRandom) 
			(info "Random move")  
			(priority 100) 
		)
	)
)	

;; HUIDA ;;
(defrule INKYpacmanFarAway
   (INKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
   (test (> ?e 0))
   (test (or (neq ?t 0) (> ?d (+ (/ ?e 2) 1))))  ;; far away if distance > (edibleTime/2 + 1)
	=>
   (assert
      (ACTION 
         (id INKYOrbit)
         (info "INKY far away and edible")
         (priority 21) 	
      )
   )
)

(defrule INKYhayEscudero
	(INKY (myShield ?g) (edibleTime ?e))
	(test (> ?e 0))
	=>
   	(assert
    	(ACTION 
        	(id INKYRunToEscudero)
         	(info "INKY going to escudero")
         	(extraGhost ?g)
         	(priority 20) 		
      	)
	)
)

(defrule INKYpacmanNear
   (INKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
   (test (> ?e 0))
   (test (or (== ?t 0) (< ?d 200)))  ;; near if distance < 200
	=>
   (assert
      (ACTION 
         (id INKYrunsOptimal)
         (info "INKY near and edible")
         (priority 19) 		
      )
   )
)

(defrule INKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (distanceToClosestPPill ?d)) 
	(test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION 
			(id BLINKYstatrRunning) 
			(info "MSPacMan cerca PPill") 
			(priority 18) 
		)
	)
)

;; PERSECUCION ;;
(defrule INKYediblesNearPacman
	(BLINKY	(myShield ?b))
	(PINKY 	(myShield ?p)) 
	(SUE   	(myShield ?s))
	(test (or 
		((= ?p INKY)
		(bind ?protegee ?p))
		((= ?b INKY)
		(bind ?protegee ?b))
		((= ?s INKY)
		(bind ?protegee ?s))
		)
	)
	=>
	(assert 
		(ACTION 
			(id INKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost ?protegee) 
			(priority 17)
		)
	)
)

(defrule INKYNearestToMsPacman
	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	(test (<= ?inkyDistance ?pinkyDistance))
	(test (<= ?inkyDistance ?blinkyDistance))
	(test (<= ?inkyDistance ?sueDistance))
	=> 
	(assert 
		(ACTION 
			(id INKYHunter1) 
			(info "Soy cazador1")  
			(priority 15) 
		)
	)
)

(defrule INKYSecondNearestToMsPacman
  	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	(test
		(or
			(and (> ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?blinkyDistance)
				(<= ?inkyDistance ?sueDistance)
				(bind ?closestGhost PINKY))
			(and (> ?inkyDistance ?blinkyDistance)
				(<= ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?sueDistance)
				(bind ?closestGhost INKY))
			(and (> ?inkyDistance ?sueDistance)
				(<= ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?blinkyDistance)
				(bind ?closestGhost SUE))
    	)
  	)
  =>
  	(assert 
  		(ACTION 
			(id INKYHunter2) 
			(info "Soy Hunter2") 
			(extraGhost closestGhost) 
			(priority 14)
		)
	)
)

(defrule INKYNearestToIntersection
	(MSPACMAN 	(closestIntersection 	?closestintersection))
	(BLINKY 	(distanceToIntersection ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 		(distanceToIntersection ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  		(distanceToIntersection ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE		(distanceToIntersection ?sueDistance))       	; Hecho para la distancia de Sue
	(test (<= ?inkyDistance ?pinkyDistance))
	(test (<= ?inkyDistance ?blinkyDistance))
	(test (<= ?inkyDistance ?sueDistance))
	=> 
	(assert 
		(ACTION 
			(id INKYJailer) 
			(info "Soy Jailer") 
			(intersection ?closestintersection) 
			(priority 13) 
		)
	)
)