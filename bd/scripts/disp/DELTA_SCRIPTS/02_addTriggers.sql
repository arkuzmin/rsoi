-- ������� �� ������������� ��� ������ ������
CREATE TRIGGER RSOI_DISP.CONFIRM_APPLICATION_TRG BEFORE UPDATE ON RSOI_DISP.APPLICATION
  FOR EACH ROW
  BEGIN 
  IF NEW.APPLICATION_ST <> OLD.APPLICATION_ST THEN
    IF NEW.APPLICATION_ST = 'CONFIRMED' THEN
      IF NEW.USER_IDENTIFIER = 'R' THEN 
        INSERT INTO RSOI_DISP.USER_ORDERS (ORDER_GUID, USER_GUID, ORDER_DETAIL_GUID) VALUES (NEW.APPLICATION_GUID, NEW.REQUESTER_GUID, NEW.ORDER_DETAIL_GUID);
      END IF;
      IF NEW.USER_IDENTIFIER = 'G' THEN 
        INSERT INTO RSOI_DISP.GUEST_ORDERS (ORDER_GUID, GUEST_GUID, ORDER_DETAIL_GUID) VALUES (NEW.APPLICATION_GUID, NEW.REQUESTER_GUID, NEW.ORDER_DETAIL_GUID);
      END IF;
    END IF;
  END IF;
  END 