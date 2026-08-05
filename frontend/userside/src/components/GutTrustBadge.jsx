import { ShieldCheck } from "lucide-react";

function GutTrustBadge({ score }) {
  const safeScore =
    score === null || score === undefined
      ? 0
      : Number(score);

  return (
    <div className="guttrust-badge">
      <ShieldCheck size={16} />
      <span>GutTrust</span>
      <strong>{safeScore.toFixed(1)}</strong>
    </div>
  );
}

export default GutTrustBadge;