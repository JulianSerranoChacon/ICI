;; DEFINITION OF DATA TYPES ;;

(deftemplate MSPACMAN
	(slot distanceToBlinky 			(type float) (default -1))
	(slot distanceToPinky 			(type float) (default -1))
	(slot distanceToInky 			(type float) (default -1))
	(slot distanceToSue 			(type float) (default -1))
	(slot closestIntersection 		(type integer) (default -1))
	(slot distanceToClosestPPill 	(type float) (default -1))	
)

(deftemplate BLINKY
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToPinky 			(type float)  (default -1))
	(slot distanceToInky 			(type float)  (default -1))
	(slot distanceToSue 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate INKY
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToBlinky 			(type float)  (default -1))
	(slot distanceToPinky 			(type float)  (default -1))
	(slot distanceToSue 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate PINKY
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToBlinky 			(type float)  (default -1))
	(slot distanceToInky 			(type float)  (default -1))
	(slot distanceToSue 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate SUE
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToBlinky 			(type float)  (default -1))
	(slot distanceToPinky 			(type float)  (default -1))
	(slot distanceToInky 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate ROLES
	(slot hunter1					(type SYMBOL) (default ""))
	(slot hunter2					(type SYMBOL) (default ""))
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

;; DEDUCED INFORMATION ;;
(defrule BLINKYHunter1
	(declare (salience 50))
	(BLINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	
	;(test (<= ?t 0))
	(test
		(and (<= ?blinkyDistance ?pinkyDistance)
			(<= ?blinkyDistance ?inkyDistance)
			(<= ?blinkyDistance ?sueDistance)
		)
	)
	=>
	(assert (ROLES (hunter1 BLINKY)))
)

(defrule PINKYHunter1
   (declare (salience 50))
   (PINKY (lairTime ?t))
   (BLINKY (distanceToPacman ?blinkyDistance))
   (PINKY  (distanceToPacman ?pinkyDistance))
   (INKY   (distanceToPacman ?inkyDistance))
   (SUE    (distanceToPacman ?sueDistance))

   (test (<= ?t 0))
   (test (and (<= ?pinkyDistance ?blinkyDistance)
              (<= ?pinkyDistance ?inkyDistance)
              (<= ?pinkyDistance ?sueDistance)))
   =>
   (assert (ROLES (hunter1 PINKY)))
)

(defrule INKYHunter1
   (declare (salience 50))
   (INKY (lairTime ?t))
   (BLINKY (distanceToPacman ?blinkyDistance))
   (PINKY  (distanceToPacman ?pinkyDistance))
   (INKY   (distanceToPacman ?inkyDistance))
   (SUE    (distanceToPacman ?sueDistance))

   (test (<= ?t 0))
   (test (and (<= ?inkyDistance ?blinkyDistance)
              (<= ?inkyDistance ?pinkyDistance)
              (<= ?inkyDistance ?sueDistance)))
   =>
   (assert (ROLES (hunter1 INKY)))
)

(defrule SUEHunter1
   (declare (salience 50))
   (SUE (lairTime ?t))
   (BLINKY (distanceToPacman ?blinkyDistance))
   (PINKY  (distanceToPacman ?pinkyDistance))
   (INKY   (distanceToPacman ?inkyDistance))
   (SUE    (distanceToPacman ?sueDistance))

   (test (<= ?t 0))
   (test (and (<= ?sueDistance ?blinkyDistance)
              (<= ?sueDistance ?pinkyDistance)
              (<= ?sueDistance ?inkyDistance)))
   =>
   (assert (ROLES (hunter1 SUE)))
)

(defrule BLINKYHunter2
	(declare (salience 49))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))       
	(test
		(and 
			(or (eq ?h1 PINKY) (<= ?blinkyDistance ?pinkyDistance))
			(or (eq ?h1 INKY)  (<= ?blinkyDistance ?inkyDistance))
			(or (eq ?h1 SUE)   (<= ?blinkyDistance ?sueDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 BLINKY))
)

(defrule PINKYHunter2
	(declare (salience 49))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))       
	(test
		(and 
			(or (eq ?h1 BLINKY) (<= ?pinkyDistance ?blinkyDistance))
			(or (eq ?h1 INKY)   (<= ?pinkyDistance ?inkyDistance))
			(or (eq ?h1 SUE)    (<= ?pinkyDistance ?sueDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 PINKY))
)

(defrule INKYHunter2
	(declare (salience 49))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))       
	(test
		(and 
			(or (eq ?h1 BLINKY) (<= ?inkyDistance ?blinkyDistance))
			(or (eq ?h1 PINKY)  (<= ?inkyDistance ?pinkyDistance))
			(or (eq ?h1 SUE)    (<= ?inkyDistance ?sueDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 INKY))
)

(defrule SUEHunter2
	(declare (salience 49))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))       
	(test
		(and 
			(or (eq ?h1 BLINKY) (<= ?sueDistance ?blinkyDistance))
			(or (eq ?h1 PINKY)  (<= ?sueDistance ?pinkyDistance))
			(or (eq ?h1 INKY)   (<= ?sueDistance ?inkyDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 SUE))
)

;; LAIR ;;
(defrule INKYinlair
	(declare (salience 100))
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
	(declare (salience 21))
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
	(declare (salience 20))
	(INKY (myShield ?g) (edibleTime ?e))
	(test (> ?e 0))
	(test (neq ?g ""))
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
	(declare (salience 19))
	(INKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
	(test (> ?e 0))
	(test (and (= ?t 0) (< ?d 200)))  ;; near if distance < 200
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
	(declare (salience 18))
	(MSPACMAN (distanceToClosestPPill ?d)) 
	(test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION 
			(id INKYstatrRunning) 
			(info "MSPacMan cerca PPill") 
			(priority 18) 
		)
	)
)

;; PERSECUCION ;;
(defrule Shieldblinky
	(declare (salience 17))
	(BLINKY (myShield ?b))
	(test (eq ?b INKY))
	=>
	(assert 
		(ACTION 
			(id INKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost BLINKY) 
			(priority 17)
		)
	)
)

(defrule ShieldPinky
	(declare (salience 17))
	(PINKY (myShield ?p))
	(test (eq ?p INKY))
	=>
	(assert 
		(ACTION 
			(id INKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost PINKY) 
			(priority 17)
		)
	)
)

(defrule ShieldSue
	(declare (salience 17))
	(SUE (myShield ?s))
	(test (eq ?s INKY))
	=>
	(assert 
		(ACTION 
			(id INKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost SUE) 
			(priority 17)
		)
	)
)


(defrule INKYNearestToMsPacman
	(declare (salience 15))
	(ROLES (hunter1 ?h1))
	(test (eq ?h1 INKY))
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
	(declare (salience 14))
	(ROLES (hunter1 ?h1) (hunter2 ?h2))
	(test (eq ?h2 INKY))
	=>
	(assert 
  		(ACTION 
			(id INKYHunter2) 
			(info "Soy Hunter2") 
			(extraGhost ?h1) 
			(priority 14)
		)
	)
)

(defrule INKYNearestToIntersection
	(declare (salience 13))
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

(defrule Random
	(declare (salience -1))
	=>
	(assert 
		(ACTION 
			(id INKYRandom) 
			(info "Random move")  
			(priority 12) 
		)
	)
)