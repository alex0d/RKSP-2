// SPDX-License-Identifier: MIT
pragma solidity ^0.8.27;

contract SecuritiesContract {
    struct Security {
        uint id;
        address owner;
        string name;
        uint price;
    }

    mapping(uint => Security) public securities;
    uint public securityCount;

    event SecurityBought(uint indexed id, address indexed buyer);
    event SecuritiesExchanged(uint indexed securityId1, uint indexed securityId2);

    function addSecurity(string memory _name, uint _price) public {
        securities[securityCount] = Security(securityCount, msg.sender, _name, _price);
        securityCount++;
    }

    function buySecurity(uint _id) public payable {
        Security storage security = securities[_id];
        require(msg.value >= security.price, "Not enough funds to buy this security.");
        require(security.owner != msg.sender, "You cannot buy your own security.");

        address payable seller = payable(security.owner);
        seller.transfer(security.price);
        
        uint excess = msg.value - security.price;
        if (excess > 0) {
            payable(msg.sender).transfer(excess);
        }
        
        security.owner = msg.sender;

        emit SecurityBought(_id, msg.sender);
    }

    function exchangeSecurities(uint _id1, uint _id2) public {
        Security storage security1 = securities[_id1];
        Security storage security2 = securities[_id2];

        require(security1.owner == msg.sender || security2.owner == msg.sender, "You must own one of the securities to exchange.");
        address temp = security1.owner;
        security1.owner = security2.owner;
        security2.owner = temp;

        emit SecuritiesExchanged(_id1, _id2);
    }
}
