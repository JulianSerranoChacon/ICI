;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpil (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false))
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate INKY
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpil (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false)) 
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate PINKY
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpil (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false)) 
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)

(deftemplate SUE
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpil (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false))
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate MSPACMAN 
 	(slot voyGreedy (type SYMBOL) (default true)) 
	(slot hayPillEnCaminoInmediato (type SYMBOL) (default false)) 
    (slot minDistancePPill (type NUMBER) (default 100000)) 
	(slot variosCaminos (type NUMBER) (default 0))
	(slot quedanPPils (type SYMBOL) (default false))
	(slot tiempoDesdePpil (type NUMBER) (default 0))

	(slot distanceToBLINKY (type NUMBER)(default 100000))
	(slot distanceToINKY (type NUMBER)(default 100000))
	(slot distanceToPINKY (type NUMBER)(default 100000))
	(slot distanceToSUE (type NUMBER)(default 100000))

	(slot dangerDistanceGhost (type NUMBER)(default 100000))

	(slot distanceToEatBLINKY (type NUMBER)(default 100000))
	(slot distanceToEatINKY (type NUMBER)(default 100000))
	(slot distanceToEatPINKY (type NUMBER)(default 100000))
	(slot distanceToEatSUE (type NUMBER)(default 100000))

	(slot numEatableGhost (type NUMBER) (default 0))
	(slot numDangerGhosts (type NUMBER) (default 0))
	(slot llegoAntesAPPil (type SYMBOL) (default true))
	
	(slot goToPillMove (type SYMBOL) (default false))
	
	(slot RIGHTCandidate (type SYMBOL) (default false))
	(slot LEFTCandidate (type SYMBOL)(default false))
	(slot UPCandidate (type SYMBOL)(default false))
	(slot DOWNCandidate (type SYMBOL)(default false))
	
	(slot ClosestPpil (type NUMBER) (default 0))
	
	(slot RIGHTMoveToPpill (type SYMBOL)(default false))
	(slot LEFTMoveToPpill (type SYMBOL)(default false))
	(slot UPMoveToPpill (type SYMBOL)(default false))
	(slot DOWNMoveToPpill (type SYMBOL)(default false))
	
	(slot RIGHTMoveToPoints (type NUMBER)(default 100000))
	(slot LEFTMoveToPoints (type NUMBER)(default 100000))
	(slot UPMoveToPoints (type NUMBER)(default 100000))
	(slot DOWNMoveToPoints (type NUMBER)(default 100000))
	
	(slot RIGHTMoveToNode (type NUMBER)(default 100000))
	(slot LEFTMoveToNode (type NUMBER)(default 100000))
	(slot UPMoveToNode (type NUMBER)(default 100000))
	(slot DOWNMoveToNode (type NUMBER)(default 100000))
)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots

	(slot CandidateLeft (type SYMBOL)) ; Extra slot for the runaway action
	(slot CandidateRight (type SYMBOL))
	(slot CandidateUp (type SYMBOL))
	(slot CandidateDown (type SYMBOL))

	(slot MoveToPpillLeft (type SYMBOL)) 
	(slot MoveToPpillRight (type SYMBOL))
	(slot MoveToPpillUp (type SYMBOL))
	(slot MoveToPpillDown (type SYMBOL))

	(slot MoveToPointsLeft (type NUMBER)) 
	(slot MoveToPointsRight (type NUMBER))
	(slot MoveToPointsUp (type NUMBER))
	(slot MoveToPointsDown (type NUMBER))

	(slot MoveToNodeLeft (type NUMBER)) 
	(slot MoveToNodeRight (type NUMBER))
	(slot MoveToNodeUp (type NUMBER))
	(slot MoveToNodeDown (type NUMBER))

	(slot closerPpil (type NUMBER))

) 


;TEMPLATES ASIGNADOS EN EL CLP

; Compruebo cuantos fantasmas cercanos no comestibles hay
;BLINKY
(defrule BlinkyNoComestibleYCerca
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (BLINKY (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be false)) (test (?bd <= ?d))
	=>
	 (modify ?b (estoyCercaYsoyPeligroso true))
)
;Añado a Blinky a peligroso si es que este lo es
(defrule BlinkySeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(BLINKY (estoyCercaYsoyPeligroso true))
	=>
	(modify ?m (numDangerGhosts (?n + 1)))
)

;INKY
(defrule InkyNoComestibleYCerca
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (INKY (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be false)) (test (?bd <= ?d))
	=>
	(modify ?b (estoyCercaYsoyPeligroso true))
)
;Añado a Inky a peligroso si es que este lo es
(defrule InkySeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(INKY (estoyCercaYsoyPeligroso true))
	=>
	(modify ?m (numDangerGhosts (?n + 1)))
)

;PINKY
(defrule Pinky
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (PINKY (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be false)) (test (?bd <= ?d))
	=>
	(modify ?b (estoyCercaYsoyPeligroso true))
)
;Añado a Pinky a peligroso si es que este lo es
(defrule PinkySeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(PINKY (estoyCercaYsoyPeligroso true))
	=>
	(modify ?m (numDangerGhosts (?n + 1)))
)

;SUE
(defrule SueNoComestibleYCerca
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (SUE (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be false)) (test (?bd <= ?d))
	=>
	(modify ?b (estoyCercaYsoyPeligroso true))
)
;Añado a Sue a peligroso si es que este lo es
(defrule SueSeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(SUE (estoyCercaYsoyPeligroso true))
	=>
	(modify ?m (numDangerGhosts (?n + 1)))
)


;COMPROBACION DE QUIEN LLEGA ANTES A LA PPIL
;BLINKY
(defrule BlinkyHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (BLINKY (edible ?e) (minDistancePpil ?bd))
	(test (?e false)) (test(?bd < ?md))
	=>
	(modify ?b (llegoAntesAPPil true))
)

;Informo a PacMan de que no llega a la PPIL
(defrule BlinkySeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(BLINKY (llegoAntesAPPil true))
	=>
	(modify ?m (llegoAntesAPPil false))
)
;INKY 	
(defrule InkyHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (INKY (edible ?e) (minDistancePpil ?bd))
	(test (?e false)) (test(?bd < ?md))
	=>
	(modify ?b (llegoAntesAPPil true))
)

;Informo a PacMan de que no llega a la PPIL
(defrule InkySeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(INKY (llegoAntesAPPil true))
	=>
	(modify ?m (llegoAntesAPPil false))
)
;PINKY
(defrule PinkyHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (PINKY (edible ?e) (minDistancePpil ?bd))
	(test (?e false)) (test(?bd < ?md))
	=>
	(modify ?b (llegoAntesAPPil true))
)

;Informo a PacMan de que no llega a la PPIL
(defrule PinkySeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(PINKY (llegoAntesAPPil true))
	=>
	(modify ?m (llegoAntesAPPil false))
)
;SUE
(defrule SueHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (SUE (edible ?e) (minDistancePpil ?bd))
	(test (?e false)) (test(?bd < ?md))
	=>
	(modify ?b (llegoAntesAPPil true))
)

;Informo a PacMan de que no llega a la PPIL
(defrule SueSeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(SUE (llegoAntesAPPil true))
	=>
	(modify ?m (llegoAntesAPPil false))
)


;COMPROBACIÓN DE CUANTOS FANTASMAS COMESTIBLES HAY
;Blinky
(defrule BlinkySePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToBLINKY ?d) (distanceToEatBLINKY ?de))
	(BLINKY (edible ?e))
	(test(?e true)) (test(?d <= ?de))
	=>
	(modify ?m (numEatableGhost (?n + 1)))
)
;Inky
(defrule InkySePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToINKY ?d) (distanceToEatINKY ?de))
	(INKY (edible ?e))
	(test(?e true)) (test(?d <= ?de))
	=>
	(modify ?m (numEatableGhost (?n + 1)))
)
;Pinky
(defrule PinkySePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToPINKY ?d) (distanceToEatPINKY ?de))
	(PINKY (edible ?e))
	(test(?e true)) (test(?d <= ?de))
	=>
	(modify ?m (numEatableGhost (?n + 1)))
)
;Sue
(defrule SueSePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToSUE ?d) (distanceToEatSUE ?de))
	(SUE (edible ?e))
	(test(?e true)) (test(?d <= ?de))
	=>
	(modify ?m (numEatableGhost (?n + 1)))
)

;RULES 
; Comienzo siempre en ir a comer pills, luego iré viendo que tengo que hacer realmente
(defrule MSPacManMoveToClosestPill
	( MSPACMAN 
		(voyGreedy true) 
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
	) 
	=>
	(assert
		(
			ACTION 
				(id "Goes nearest pill action") 
				(info "Soy un greedy") 
				(priority 0)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
		)
	)
)

; Si hay pills inmediatas voy a por ellas
(defrule MSPacManGetMorePoints
	( MSPACMAN
		(hayPillEnCaminoInmediato true) 	
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
		(RIGHTMoveToPpill ?rpp) (LEFTMoveToPpill ?lpp) (UPMoveToPpill ?upp) (DOWNMoveToPpill ?dpp)
		(RIGHTMoveToPoints ?rp) (LEFTMoveToPoints ?lp) (UPMoveToPoints ?up) (DOWNMoveToPoints ?dp)
	)
	=>
	(assert
		(
			ACTION 
				(id "Greedy Points Action") 
				(info "A por más puntos") 
				(priority 1)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
				(MoveToPpillLeft ?lpp)
				(MoveToPpillRight ?rpp)
				(MoveToPpillUp ?upp)
				(MoveToPpillDown ?dpp)
				(MoveToPointsLeft ?lp)
				(MoveToPointsRight ?rp)
				(MoveToPointsUp ?up)
				(MoveToPointsDown ?dp)
		)
	)
)

; Si solo hay un fantasma cerca de mi huyo de el
(defrule MSPacManEscapeFromOne
	( MSPACMAN 
		(numDangerGhosts ?n)
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
	) 
	(test (= ?n 1))
	=>
	(assert
		(
			ACTION 
				(id "Hide From One Action") 
				(info "Huyo de un fantasma") 
				(priority 2)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
		)
	)
)

; Si hay muchos fantasmas cerca de mi, intento comerme la PPIL

(defrule MSPacManTryPPIL
	( MSPACMAN 
		(numDangerGhosts ?n)
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
		(ClosestPpil ?cpp)
	)
	(test (>?n 1))
	=>
	(assert
		(
			ACTION 
				(id "Go PPill Action") 
				(info "IntentoAcercarmeAunaPPIL") 
				(priority 3)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
				(closerPpil ?cpp)
		)
	)
)

; Si tengo muchos fantasmas persiguiendome y llego a la powerPill voy a por ella

(defrule MSPacManEatPPIL
	(MSPACMAN (llegoAntesAPPil ?b) (numDangerGhosts ?n) (minDistancePPill ?m))
	(test(?b true))  (test(> ?n 1))  (test( <= ?m 50)) ;50 puesto a ojo

	=>
	(assert
		(
			ACTION (id "Eat PPill Action") (info "Me he comido una PPIl") (priority 4)
		)
	)
)

; Si hay fantasmas comestibles cerca y no hay fantasmas no comestibles cerca me voy a comerlos 
(defrule MSPacManStartsFollowing
	(MSPACMAN (numDangerGhosts ?nd) (numEatableGhost ?ne) )
	(test (= ?nd 0))  (test (> ?ne 0))
	=>
	(assert
		(
			ACTION (id "Hunt Action") (info "voy a comer") (priority 5)
		)
	)
)

;Si no hay caminos disponibles intento ir a por la PPIL
;FALTA COMPROBAR SI LLEGO A COMERLA
(defrule MSPacManStartSuicida
	(MSPACMAN (variosCaminos ?v))
	(test(= ?v 0)) 
	=>
	(assert
		(
			ACTION (id "PPill Suicida") (info "Intento huir a la powerPPil") (priority 6)
		)
	)
)

; Si no hay caminos disponibles, ni PPILS pero hay pils accesibles, me muevo a esas pills
(defrule MSPacManPillsSuicida
	(MSPACMAN (variosCaminos ?v) (quedanPPils ?p))
	(test(= ?v 0)) (test(= ?p 0))
	=>
	(assert
		(
			ACTION (id "Go pills suicida action") (info "A pillar la mayor cantidad de puntos que pueda") (priority 7)
		)
	)
)

;Si no hay caminos disponibles, ni PPILS, ni PILLS me muevo random
(defrule MSPacManRandom
	(MSPACMAN (variosCaminos ?v) (quedanPPils ?p) (hayPillEnCaminoInmediato ?b)) 
	(test(= ?v 0)) (test(= ?p 0)) (test(?b false))
	=>
	(assert
		(
			ACTION (id "Random Action") (info "Todo al verde") (priority 8)
		)
	)
)
;Si solo se puede mover a un lado da igual todo lo demás debemos ir a ese camino
(defrule MSPacManSoloUnMovimiento
	(MSPACMAN (variosCaminos ?v))
	(test(= ?v 1))
	=>
	(assert
		(
			ACTION (id "Only move possible action") (info "Solo tengo un movimiento posible") (priority 19)
		)
	)
)	