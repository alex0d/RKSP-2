import { expect } from "chai";
import { ethers } from "hardhat";
import { SecuritiesContract } from "../typechain-types";
import { loadFixture } from "@nomicfoundation/hardhat-toolbox/network-helpers";

describe("SecuritiesContract", function () {
  async function deploySecuritiesFixture() {
    const [owner, user1, user2] = await ethers.getSigners();
    const Securities = await ethers.getContractFactory("SecuritiesContract");
    const securities = await Securities.deploy() as SecuritiesContract;

    return { securities, owner, user1, user2 };
  }

  it("Should add a new security", async function () {
    const { securities, owner } = await loadFixture(deploySecuritiesFixture);

    const tx = await securities.addSecurity("Test Stock", ethers.parseEther("0.1"));
    await tx.wait();

    const securityCount = await securities.securityCount();
    expect(securityCount).to.equal(1n);

    const security = await securities.securities(0);
    expect(security.name).to.equal("Test Stock");
    expect(security.price).to.equal(ethers.parseEther("0.1"));
    expect(security.owner).to.equal(owner.address);
  });

  it("Should allow buying securities", async function () {
    const { securities, user1 } = await loadFixture(deploySecuritiesFixture);
    
    await securities.addSecurity("Test Stock", ethers.parseEther("0.1"));
    await securities.connect(user1).buySecurity(0, { value: ethers.parseEther("0.1") });
    
    const security = await securities.securities(0);
    expect(security.owner).to.equal(user1.address);
  });

  it("Should allow exchanging securities", async function () {
    const { securities, owner, user1 } = await loadFixture(deploySecuritiesFixture);
    
    await securities.addSecurity("Stock A", ethers.parseEther("0.1"));
    await securities.connect(user1).addSecurity("Stock B", ethers.parseEther("0.2"));
    
    await securities.connect(user1).buySecurity(0, { value: ethers.parseEther("0.1") });
    await securities.connect(owner).buySecurity(1, { value: ethers.parseEther("0.2") });
    
    await securities.exchangeSecurities(0, 1);
    
    const security1 = await securities.securities(0);
    const security2 = await securities.securities(1);
    
    expect(security1.owner).to.equal(owner.address);
    expect(security2.owner).to.equal(user1.address);
  });

  it("Should emit event when security is bought", async function () {
    const { securities, user1 } = await loadFixture(deploySecuritiesFixture);
    
    await securities.addSecurity("Test Stock", ethers.parseEther("0.1"));
    
    await expect(securities.connect(user1).buySecurity(0, { value: ethers.parseEther("0.1") }))
      .to.emit(securities, "SecurityBought")
      .withArgs(0n, user1.address);
  });

  it("Should emit event when securities are exchanged", async function () {
    const { securities, owner, user1 } = await loadFixture(deploySecuritiesFixture);
    
    await securities.addSecurity("Stock A", ethers.parseEther("0.1"));
    await securities.connect(user1).addSecurity("Stock B", ethers.parseEther("0.2"));
    
    await securities.connect(user1).buySecurity(0, { value: ethers.parseEther("0.1") });
    await securities.connect(owner).buySecurity(1, { value: ethers.parseEther("0.2") });
    
    await expect(securities.exchangeSecurities(0, 1))
      .to.emit(securities, "SecuritiesExchanged")
      .withArgs(0n, 1n);
  });
});