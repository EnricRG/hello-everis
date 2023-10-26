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

INSERT INTO shoplist.shop_lists (name, owner) VALUES ('fullShopList', 'userWithFullShopList');

-- Some of this products do not exist
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(1, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(2, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(3, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(4, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(5, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(6, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(7, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(8, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(9, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(10, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(11, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(12, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(13, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(14, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(15, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(16, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(17, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(18, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(19, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(20, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(21, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(22, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(23, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(24, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));
INSERT INTO shoplist.shop_lists_items (product_id, shopList_id) VALUES(25, (SELECT id FROM shoplist.shop_lists l WHERE l.name = 'fullShopList' AND l.owner = 'userWithFullShopList'));

