;; DEFINITION OF DATA TYPES ;;

(deftemplate MSPACMAN
	(slot distanceToBlinky 			(type float) (default 1000000))
	(slot distanceToPinky 			(type float) (default 1000000))
	(slot distanceToInky 			(type float) (default 1000000))
	(slot distanceToSue 			(type float) (default 1000000))
	(slot closestIntersection 		(type integer) (default 1000000))
	(slot distanceToIntersection	(type float) (default 1000000))
	(slot inCorridor				(type integer) (default 0))
	(slot distanceToClosestPPill 	(type float) (default 1000000))	
)

(deftemplate BLINKY
	(slot distanceToPacman 			(type float)  (default 1000000))
	(slot distanceToPinky 			(type float)  (default 1000000))
	(slot distanceToInky 			(type float)  (default 1000000))
	(slot distanceToSue 			(type float)  (default 1000000))
	(slot distanceToIntersection 	(type float)  (default 1000000))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate INKY
	(slot distanceToPacman 			(type float)  (default 1000000))
	(slot distanceToBlinky 			(type float)  (default 1000000))
	(slot distanceToPinky 			(type float)  (default 1000000))
	(slot distanceToSue 			(type float)  (default 1000000))
	(slot distanceToIntersection 	(type float)  (default 1000000))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate PINKY
	(slot distanceToPacman 			(type float)  (default 1000000))
	(slot distanceToBlinky 			(type float)  (default 1000000))
	(slot distanceToInky 			(type float)  (default 1000000))
	(slot distanceToSue 			(type float)  (default 1000000))
	(slot distanceToIntersection 	(type float)  (default 1000000))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate SUE
	(slot distanceToPacman 			(type float)  (default 1000000))
	(slot distanceToBlinky 			(type float)  (default 1000000))
	(slot distanceToPinky 			(type float)  (default 1000000))
	(slot distanceToInky 			(type float)  (default 1000000))
	(slot distanceToIntersection 	(type float)  (default 1000000))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate ROLES
	(slot hunter1					(type SYMBOL) (default NONE))
	(slot hunter2					(type SYMBOL) (default NONE))
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
(defrule init-roles
	(declare (salience 1000))
	(not (ROLES))
	=>
	(assert (ROLES (hunter1 NONE) (hunter2 NONE)))
)

(defrule BLINKYsavesINKY
	(declare (salience 120))
    (BLINKY     (edibleTime ?be) (lairTime ?bl) (distanceToInky ?dis))
    ?inkyFact <- (INKY (edibleTime ?ie) (lairTime ?il) (myShield ?shield))
    (MSPACMAN   (distanceToInky ?distanceFromPacman))
    (test (and 
        (<= ?il 0) (<= ?bl 0)         ;; They are not in the lair (neither shield nor shieldee)
        (<= ?be 0) (>= ?ie 0)        ;; The shield is not edible and the shieldee is edible
        (< ?distanceFromPacman (+ (/ ?ie 2) 30))    ;; And is not orbiting
        (< ?distanceFromPacman (* (/ ?dis 3) 2))    ;; And is reachable by us first
        (eq ?shield "")                                ;; And our shield doesnt need to protect anyone else
    ))
    =>
        (modify ?inkyFact (myShield "BLINKY"))
)

(defrule BLINKYsavesPINKY
	(declare (salience 120))
    (BLINKY     (edibleTime ?be) (lairTime ?bl) (distanceToPinky ?dis))
    ?pinkyFact <- (PINKY (edibleTime ?ie) (lairTime ?il) (myShield ?shield))
    (MSPACMAN     (distanceToPinky ?distanceFromPacman))
    (test (and 
        (<= ?il 0) (<= ?bl 0)         ;; They are not in the lair (neither shield nor shieldee)
        (<= ?be 0) (>= ?ie 0)        ;; The shield is not edible and the shieldee is edible
        (< ?distanceFromPacman (+ (/ ?ie 2) 30))    ;; And is not orbiting
        (< ?distanceFromPacman (* (/ ?dis 3) 2))    ;; And is reachable by us first
        (eq ?shield "")                                ;; And our shield doesnt need to protect anyone else
    ))
    =>
        (modify ?pinkyFact (myShield "BLINKY"))
)

(defrule BLINKYsavesSUE
	(declare (salience 120))
    (BLINKY     (edibleTime ?be) (lairTime ?bl) (distanceToSue ?dis))
    ?sueFact <- (SUE (edibleTime ?ie) (lairTime ?il) (myShield ?shield))
    (MSPACMAN     (distanceToSue ?distanceFromPacman))
    (test (and 
        (<= ?il 0) (<= ?bl 0)         ;; They are not in the lair (neither shield nor shieldee)
        (<= ?be 0) (>= ?ie 0)        ;; The shield is not edible and the shieldee is edible
        (< ?distanceFromPacman (+ (/ ?ie 2) 30))    ;; And is not orbiting
        (< ?distanceFromPacman (* (/ ?dis 3) 2))    ;; And is reachable by us first
        (eq ?shield "")                                ;; And our shield doesnt need to protect anyone else
    ))
    =>
        (modify ?sueFact (myShield "BLINKY"))
)

(defrule BLINKYHunter1
	(declare (salience 50))
	(BLINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))
	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test
		(and (<= ?blinkyDistance ?pinkyDistance)
			(<= ?blinkyDistance ?inkyDistance)
			(<= ?blinkyDistance ?sueDistance)
		)
	)
	=>
	
	(modify ?roles (hunter1 BLINKY))
)

(defrule PINKYHunter1
	(declare (salience 50))
	(PINKY (lairTime ?t))
	(BLINKY (distanceToPacman ?blinkyDistance))
	(PINKY  (distanceToPacman ?pinkyDistance))
	(INKY   (distanceToPacman ?inkyDistance))
	(SUE    (distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))

	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test (and (<= ?pinkyDistance ?blinkyDistance)
				(<= ?pinkyDistance ?inkyDistance)
				(<= ?pinkyDistance ?sueDistance)))
	=>
	(modify ?roles (hunter1 PINKY))
)

(defrule INKYHunter1
	(declare (salience 50))
	(INKY (lairTime ?t))
	(BLINKY (distanceToPacman ?blinkyDistance))
	(PINKY  (distanceToPacman ?pinkyDistance))
	(INKY   (distanceToPacman ?inkyDistance))
	(SUE    (distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))

	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test (and (<= ?inkyDistance ?blinkyDistance)
				(<= ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?sueDistance)))
	=>
	(modify ?roles (hunter1 INKY))
)

(defrule SUEHunter1
	(declare (salience 50))
	(SUE (lairTime ?t))
	(BLINKY (distanceToPacman ?blinkyDistance))
	(PINKY  (distanceToPacman ?pinkyDistance))
	(INKY   (distanceToPacman ?inkyDistance))
	(SUE    (distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))

	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test (and (<= ?sueDistance ?blinkyDistance)
				(<= ?sueDistance ?pinkyDistance)
				(<= ?sueDistance ?inkyDistance)))
	=>
	(modify ?roles (hunter1 SUE))
)

(defrule BLINKYHunter2
	(declare (salience 49))
	(BLINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))    
	(test (neq ?h1 NONE))
	(test (eq ?h2 NONE))   
	(test (neq ?h1 BLINKY))
	(test (<= ?t 0))
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
	(declare (salience 48))
	(PINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))   
	(test (neq ?h1 NONE))  
	(test (eq ?h2 NONE)) 
	(test (neq ?h1 PINKY))  
	(test (<= ?t 0))
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
	(declare (salience 47))
	(INKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))   
	(test (neq ?h1 NONE))   
	(test (eq ?h2 NONE))  
	(test (neq ?h1 INKY))
	(test (<= ?t 0))
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
	(declare (salience 46))
	(SUE (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))       
	(test (neq ?h1 NONE))
	(test (eq ?h2 NONE)) 
	(test (neq ?h1 SUE))
	(test (<= ?t 0))
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
(defrule BLINKYinlair
	(declare (salience 100))
	(BLINKY (lairTime ?t))
	(test (> ?t 0))
	=>
	(assert 
		(ACTION 
			(id BLINKYRandom) 
			(info "Random move")  
			(priority 100) 
		)
	)
)	

;; HUIDA ;;
(defrule BLINKYpacmanFarAway
	(declare (salience 21))
	(BLINKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
	(test (> ?e 0))
	(test (or (neq ?t 0) (> ?d (+ (/ ?e 2) 1))))  ;; far away if distance > (edibleTime/2 + 1)
		=>
	(assert
		(ACTION 
			(id BLINKYOrbit)
			(info "BLINKY far away and edible")
			(priority 21) 	
		)
	)
)

(defrule BLINKYhayEscudero
	(declare (salience 20))
	(BLINKY (myShield ?g) (edibleTime ?e))
	(test (> ?e 0))
	(test (neq ?g ""))
	=>
   	(assert
    	(ACTION 
        	(id BLINKYRunToEscudero)
         	(info "BLINKY going to escudero")
         	(extraGhost ?g)
         	(priority 20) 		
      	)
	)
)

(defrule BLINKYpacmanNear
	(declare (salience 19))
	(BLINKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
	(test (> ?e 0))
	(test (and (= ?t 0) (< ?d 200)))  ;; near if distance < 200
		=>
	(assert
		(ACTION 
			(id BLINKYrunsOptimal)
			(info "BLINKY near and edible")
			(priority 19) 		
		)
	)
)

(defrule BLINKYrunsAwayMSPACMANclosePPill
	(declare (salience 18))
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
(defrule killPacman 
	(declare (salience 17))
	(MSPACMAN (inCorridor ?c distanceToIntersection ?i))
	(BLINKY (distanceToIntersection ?d))
	(test (= ?c 1))
	(test (<= ?d (?i - 2)))
	=>
	(assert 
		(ACTION 
			(id BLINKYHunter1) 
			(info "Soy cazador1")  
			(priority 17) 
		)
	)
)

(defrule ShieldInky
	(declare (salience 16))
	(INKY (myShield ?i))
	(test (eq ?i BLINKY))
	=>
	(assert 
		(ACTION 
			(id BLINKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost INKY) 
			(priority 16)
		)
	)
)

(defrule ShieldPinky
	(declare (salience 16))
	(PINKY (myShield ?p))
	(test (eq ?p BLINKY))
	=>
	(assert 
		(ACTION 
			(id BLINKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost PINKY) 
			(priority 16)
		)
	)
)

(defrule ShieldSue
	(declare (salience 16))
	(SUE (myShield ?s))
	(test (eq ?s BLINKY))
	=>
	(assert 
		(ACTION 
			(id BLINKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost SUE) 
			(priority 16)
		)
	)
)

(defrule BLINKYNearestToMsPacman
	(declare (salience 15))
	(ROLES (hunter1 ?h1))
	(test (eq ?h1 BLINKY))
	=> 
	(assert 
		(ACTION 
			(id BLINKYHunter1) 
			(info "Soy cazador1")  
			(priority 15) 
		)
	)
)

(defrule BLINKYSecondNearestToMsPacman
	(declare (salience 14))
	(ROLES (hunter1 ?h1) (hunter2 ?h2))
	(test (neq ?h1 NONE))
	(test (eq ?h2 BLINKY))
	=>
	(assert 
  		(ACTION 
			(id BLINKYHunter2) 
			(info "Soy Hunter2") 
			(extraGhost ?h1) 
			(priority 14)
		)
	)
)	

(defrule BLINKYNearestToIntersection
	(declare (salience 13))
	(ROLES (hunter1 ?h1) (hunter2 ?h2))
	(MSPACMAN 	(closestIntersection 	?closestintersection))
	(BLINKY 	(distanceToIntersection ?blinkyDistance)) 	
	(PINKY 		(distanceToIntersection ?pinkyDistance))   
	(INKY  		(distanceToIntersection ?inkyDistance))
	(SUE		(distanceToIntersection ?sueDistance))  
	(test 	(or 
				(eq PINKY ?h1)
				(eq PINKY ?h2)
				(<= ?blinkyDistance ?pinkyDistance)
			)
	)
	(test 	(or 
				(eq INKY ?h1)
				(eq INKY ?h2)
				(<= ?blinkyDistance ?inkyDistance)
			)
	)
	(test 	(or 
				(eq SUE ?h1)
				(eq SUE ?h2)
				(<= ?blinkyDistance ?sueDistance)
			)
	)
	=> 
	(assert 
		(ACTION 
			(id BLINKYJailer) 
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
			(id BLINKYRandom) 
			(info "Random move")  
			(priority 12) 
		)
	)
)