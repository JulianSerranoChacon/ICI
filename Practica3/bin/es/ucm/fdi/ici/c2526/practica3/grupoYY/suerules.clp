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
(defrule SUEinlair
	(SUE (lairTime ?t))
	(test (> ?t 0))
	=>
	(assert 
		(ACTION 
			(id SUERandom) 
			(info "Random move")  
			(priority 100) 
		)
	)
)	

;; HUIDA ;;
(defrule SUEpacmanFarAway
   (SUE (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
   (test (> ?e 0))
   (test (or (!= ?t 0) (> ?d (+ (/ ?e 2) 1))))  ;; far away if distance > (edibleTime/2 + 1)
	=>
   (assert
      (ACTION 
         (id SUEOrbit)
         (info "SUE far away and edible")
         (priority 21) 	
      )
   )
)

(defrule SUEhayEscudero
	(SUE (myShield ?g) (edibleTime ?e))
	(test (> ?e 0))
	=>
   	(assert
    	(ACTION 
        	(id SUERunToEscudero)
         	(info "SUE going to escudero")
         	(extraGhost ?g)
         	(priority 20) 		
      	)
	)
)

(defrule SUEpacmanNear
   (SUE (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
   (test (> ?e 0))
   (test (or (== ?t 0) (< ?d 200)))  ;; near if distance < 200
	=>
   (assert
      (ACTION 
         (id SUErunsOptimal)
         (info "SUE near and edible")
         (priority 19) 		
      )
   )
)

(defrule SUErunsAwayMSPACMANclosePPill
	(MSPACMAN (distanceToClosestPPill ?d)) 
	(test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION 
			(id SUEstatrRunning) 
			(info "MSPacMan cerca PPill") 
			(priority 18) 
		)
	)
)

;; PERSECUCION ;;
(defrule SUEediblesNearPacman
	(INKY  	(myShield ?i))
	(PINKY 	(myShield ?p)) 
	(BLINKY (myShield ?s))
	(test (or 
		((= ?p SUE)
		(bind ?protegee ?p))
		((= ?i SUE)
		(bind ?protegee ?i))
		((= ?s SUE)
		(bind ?protegee ?s))
		)
	)
	=>
	(assert 
		(ACTION 
			(id SUErunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost ?protegee) 
			(priority 17)
		)
	)
)

(defrule SUENearestToMsPacman
	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	(test (<= ?sueDistance ?pinkyDistance))
	(test (<= ?sueDistance ?inkyDistance))
	(test (<= ?sueDistance ?blinkyDistance))
	=> 
	(assert 
		(ACTION 
			(id SUEHunter1) 
			(info "Soy cazador1")  
			(priority 15) 
		)
	)
)

(defrule SUESecondNearestToMsPacman
  	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
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
				(bind ?closestGhost SUE))
    	)
  	)
  =>
  	(assert 
  		(ACTION 
			(id SUEHunter2) 
			(info "Soy Hunter2") 
			(extraGhost closestGhost) 
			(priority 14)
		)
	)
)

(defrule SUENearestToIntersection
	(MSPACMAN 	(closestIntersection 	?closestintersection))
	(BLINKY 	(distanceToIntersection ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 		(distanceToIntersection ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  		(distanceToIntersection ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE		(distanceToIntersection ?sueDistance))       	; Hecho para la distancia de Sue
	(test (<= ?sueDistance ?pinkyDistance))
	(test (<= ?sueDistance ?inkyDistance))
	(test (<= ?sueDistance ?blinkyDistance))
	=> 
	(assert 
		(ACTION 
			(id SUEJailer) 
			(info "Soy Jailer") 
			(intersection ?closestintersection) 
			(priority 13) 
		)
	)
)