;; DEFINITION OF DATA TYPES ;;
(deftemplate BLINKY
	(slot edible (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL)))
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )
    
;; DEFINITION OF THE ACTION FACT (ALSO A DATA_TYPE lol) --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; Basic action
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
) 

;; Hunter1 ACTION
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER)) 

;; Hunter2 ACTION --> I just need to know the ghost hunter 1, not the entire map
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) )
	(slot hunter1Id (type SYMBOL))

;; RULES OF ALL GHOSTS --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; Basic rules
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
	
	