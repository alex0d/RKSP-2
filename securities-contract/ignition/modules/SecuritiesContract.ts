import { buildModule } from "@nomicfoundation/hardhat-ignition/modules";

export default buildModule("SecuritiesModule", (m) => {
  const securities = m.contract("SecuritiesContract");
  return { securities };
});
