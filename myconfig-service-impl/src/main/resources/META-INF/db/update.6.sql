INSERT INTO APPGRANTS (USER, APPLICATION, GRANTEDFUNCTION) SELECT USER, APPLICATION, 'app_envcreate' FROM APPGRANTS WHERE GRANTEDFUNCTION = 'app_config';

-- @rollback
DELETE FROM APPGRANTS WHERE GRANTEDFUNCTION = 'app_envcreate';