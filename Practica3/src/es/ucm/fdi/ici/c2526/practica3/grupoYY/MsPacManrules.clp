;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpill (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false))
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate INKY
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpill (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false)) 
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate PINKY
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpill (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false)) 
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)

(deftemplate SUE
	(slot edible (type SYMBOL)(default false))
	(slot minDistancePacMan (type NUMBER)(default 100000))
	(slot minDistancePpill (type NUMBER)(default 100000))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false))
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate MSPACMAN 
 	(slot voyGreedy (type SYMBOL) (default true)) 
	(slot hayPillEnCaminoInmediato (type SYMBOL) (default false)) 
    (slot minDistancePpill (type NUMBER) (default 100000)) 
	(slot variosCaminos (type NUMBER) (default 1))
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
	
	(slot RIGHTCandidate (type SYMBOL) (default true))
	(slot LEFTCandidate (type SYMBOL)(default true))
	(slot UPCandidate (type SYMBOL)(default true))
	(slot DOWNCandidate (type SYMBOL)(default true))
	
	(slot ClosestPpil (type NUMBER) (default 0))
	
	(slot RIGHTMoveToPpill (type SYMBOL)(default true))
	(slot LEFTMoveToPpill (type SYMBOL)(default true))
	(slot UPMoveToPpill (type SYMBOL)(default true))
	(slot DOWNMoveToPpill (type SYMBOL)(default true))
	
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
	(slot goPillMove (type SYMBOL))

) 

;RULES 

;Si solo se puede mover a un lado da igual todo lo demás debemos ir a ese camino
(defrule MSPacManSoloUnMovimiento
	( MSPACMAN 
		(variosCaminos ?v )
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
	)
	 (test (= ?v 1))
	=>
	(assert
		(
			ACTION 
				(id Onlymovepossibleaction) 
				(info " ") 
				(priority 2)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
		)
	)
)	

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
				(id Goesnearestpillaction) 
				(info " ") 
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
				(id GreedyPointsAction) 
				(info "") 
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

(defrule MSPacManEscapeFromAll
	(MSPACMAN (numDangerGhosts ?d) (minDistancePpill ?md)) 
	(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
	(RIGHTMoveToNode ?rn) (LEFTMoveToNode ?ln) (UPMoveToNode ?un) (DOWNMoveToNode ?dn)
	(test (> ?d  0)) 
	(test (> ?md 50))
	=>
	(assert
		(
			ACTION 
			(id HideAction) 
			(info " ") 
			(priority 10)
			(CandidateLeft ?lc)
			(CandidateRight ?rc)
			(CandidateUp ?uc)
			(CandidateDown ?dc)
			(MoveToNodeLeft ?ln)
			(MoveToNodeRight ?rn)
			(MoveToNodeUp ?un)
			(MoveToNodeDown ?dn)
		)
	)
)
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
                (id HideFromOneAction) 
                (info " ") 
                (priority 11)
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
		(numDangerGhosts ?n) (minDistancePpill ?md)
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
		(ClosestPpil ?cpp)
	)
	(test (> ?n 1)) (test(> ?md 50))
	=>
	(assert
		(
			ACTION 
				(id GoPPillAction) 
				(info " ") 
				(priority 12)
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
	( MSPACMAN
	 (llegoAntesAPPil ?b) (numDangerGhosts ?n) (minDistancePpill ?m)
	 (RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
	 (goToPillMove ?gpm)

	)
	(test(eq ?b true))  
	(test(> ?n 1))  
	(test( <= ?m 50)) ;50 puesto a ojo

	=>
	(assert
		(
			ACTION 
				(id EatPPillAction) 
				(info " ") 
				(priority 14)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
				(goPillMove ?gpm)
		)
	)
)

; Si hay fantasmas comestibles cerca y no hay fantasmas no comestibles cerca me voy a comerlos 
(defrule MSPacManStartsFollowing
	( MSPACMAN 
		(numDangerGhosts ?nd) 
		(numEatableGhost ?ne) 
		(RIGHTCandidate ?rc) (LEFTCandidate ?lc) (UPCandidate ?uc) (DOWNCandidate ?dc)
	)
	(test (= ?nd 0)) (test (> ?ne 0))
	=>
	(assert
		(
			ACTION 
				(id HuntAction) 
				(info " ") 
				(priority 69)
				(CandidateLeft ?lc)
				(CandidateRight ?rc)
				(CandidateUp ?uc)
				(CandidateDown ?dc)
		)
	)
)

;Si no hay caminos disponibles intento ir a por la PPIL
;FALTA COMPROBAR SI LLEGO A COMERLA
(defrule MSPacManStartSuicida
	( MSPACMAN 
		(variosCaminos ?v)
		(RIGHTMoveToPpill ?rpp) (LEFTMoveToPpill ?lpp) (UPMoveToPpill ?upp) (DOWNMoveToPpill ?dpp)
	)
	(test(= ?v 0)) 
	=>
	(assert
		(
			ACTION 
				(id PPillSuicida) 
				(info " ") 
				(priority 6)
				(MoveToPpillLeft ?lpp)
				(MoveToPpillRight ?rpp)
				(MoveToPpillUp ?upp)
				(MoveToPpillDown ?dpp)
		)
	)
)

; Si no hay caminos disponibles, ni PPILS pero hay pils accesibles, me muevo a esas pills
(defrule MSPacManPillsSuicida
	( MSPACMAN 
		(variosCaminos ?v) (quedanPPils ?p)
		(RIGHTMoveToPoints ?rp) (LEFTMoveToPoints ?lp) (UPMoveToPoints ?up) (DOWNMoveToPoints ?dp)
	)
	(test(= ?v 0)) 
	(test(= ?p 0))
	=>
	(assert
		(
			ACTION 
				(id Gopillssuicidaaction) 
				(info " ") 
				(priority 7)
				(MoveToPointsLeft ?lp)
				(MoveToPointsRight ?rp)
				(MoveToPointsUp ?up)
				(MoveToPointsDown ?dp)
		)
	)
)

;Si no hay caminos disponibles, ni PPILS, ni PILLS me muevo random
(defrule MSPacManRandom
	( MSPACMAN 
		(variosCaminos ?v) (quedanPPils ?p) (hayPillEnCaminoInmediato ?b)
	) 
	(test(= ?v 0)) 
	(test(= ?p 0)) 
	(test(eq ?b false))
	=>
	(assert
		(
			ACTION 
				(id RandomAction) 
				(info " ") 
				(priority 8)
		)
	)
)
