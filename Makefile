all:
	$(MAKE) -C lib/po-uilib
	$(MAKE) -C ggc-core
	$(MAKE) -C ggc-app

clean:
	$(MAKE) -C lib/po-uilib clean
	$(MAKE) -C ggc-core clean
	$(MAKE) -C ggc-app clean
