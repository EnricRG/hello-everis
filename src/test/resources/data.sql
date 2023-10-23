INSERT INTO shoplist.shop_lists (name, owner) VALUES ('list1', 'userWith5Lists');
INSERT INTO shoplist.shop_lists (name, owner) VALUES ('list2', 'userWith5Lists');
INSERT INTO shoplist.shop_lists (name, owner) VALUES ('list3', 'userWith5Lists');
INSERT INTO shoplist.shop_lists (name, owner) VALUES ('list4', 'userWith5Lists');
INSERT INTO shoplist.shop_lists (name, owner) VALUES ('list5', 'userWith5Lists');

INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(1, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'list1' AND l.owner = 'userWith5Lists'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(1, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'list2' AND l.owner = 'userWith5Lists'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(1, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'list3' AND l.owner = 'userWith5Lists'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(1, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'list4' AND l.owner = 'userWith5Lists'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(1, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'list5' AND l.owner = 'userWith5Lists'));