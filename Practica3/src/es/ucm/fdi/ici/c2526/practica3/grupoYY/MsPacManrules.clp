;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL)))
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) 
	(slot hayFantasmasCerca (type SYMBOL))
	(slot soloUnaInterseccionPosible (type SYMBOL))
	(slot variosCaminos (type NUMBER))
	(slot quedanPPils (type SYMBOL))
	(slot estoyCercaDePpil (type SYMBOL))
	(slot hayFantasmasCercaDePpil(type SYMBOL))
	(slot tiempoDesdePpil (type NUMBER))
	(slot distanceToBLINKY (type NUMBER))
	(slot distanceToINKY (type NUMBER))
	(slot distanceToPINKY (type NUMBER))
	(slot distanceToSUE (type NUMBER))
	)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
) 

;RULES 
(defrule BLINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill") (priority 50) 
			(runawaystrategy RANDOM)
		)
	)
)

(defrule BLINKYrunsAway
	(BLINKY (edible true)) 
	=>  
	(assert 
		(ACTION (id BLINKYrunsAway) (info "Comestible --> huir") (priority 30) 
			(runawaystrategy CORNER)
		)
	)
)
	
(defrule BLINKYchases
	(BLINKY (edible false)) 
	=> 
	(assert (ACTION (id BLINKYchases) (info "No comestible --> perseguir")  (priority 10) ))
)	
	
	