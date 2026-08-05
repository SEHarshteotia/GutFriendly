import {
  Award,
  ClipboardCheck,
  Gift,
  ShieldCheck,
  Star,
  Store,
} from "lucide-react";

const trustSteps = [
  {
    icon: Store,
    title: "Vendor registration",
    description:
      "Food businesses register with their shop, contact and operational details before joining the platform.",
  },
  {
    icon: ClipboardCheck,
    title: "Hygiene inspection",
    description:
      "Inspectors assess food storage, cleanliness, staff hygiene, sanitation and safety practices.",
  },
  {
    icon: Award,
    title: "Verification and certification",
    description:
      "Eligible establishments receive verified status and certification based on inspection results.",
  },
  {
    icon: Star,
    title: "Verified customer reviews",
    description:
      "Only customers with delivered orders can submit reviews, helping reduce fake or unrelated feedback.",
  },
  {
    icon: ShieldCheck,
    title: "GutTrust score",
    description:
      "Inspection performance and customer feedback are combined into one easy-to-understand trust score.",
  },
  {
    icon: Gift,
    title: "Responsible feedback rewards",
    description:
      "Users earn reward points for meaningful feedback while rewards remain limited to one grant per order.",
  },
];

function TrustProcessPage() {
  return (
    <div className="trust-process-page section-container">
      <section className="trust-process-hero">
        <p className="home-eyebrow">
          Transparent food safety
        </p>

        <h1>How GutFriendly builds trust</h1>

        <p>
          We combine verified inspections,
          certifications and genuine customer
          experiences to help people make safer food
          choices.
        </p>
      </section>

      <section className="trust-steps-grid">
        {trustSteps.map((step, index) => {
          const Icon = step.icon;

          return (
            <article
              className="trust-step-card"
              key={step.title}
            >
              <div className="trust-step-number">
                {String(index + 1).padStart(2, "0")}
              </div>

              <div className="trust-step-icon">
                <Icon size={25} />
              </div>

              <h2>{step.title}</h2>
              <p>{step.description}</p>
            </article>
          );
        })}
      </section>

      <section className="trust-score-explanation">
        <div>
          <p className="home-eyebrow">
            Score methodology
          </p>

          <h2>
            One score, multiple trust signals
          </h2>

          <p>
            The final GutTrust score reflects both
            formal inspection performance and verified
            customer experience.
          </p>
        </div>

        <div className="trust-formula-card">
          <div className="trust-formula-row">
            <span>Inspection score</span>
            <strong>70%</strong>
          </div>

          <div className="trust-formula-plus">+</div>

          <div className="trust-formula-row">
            <span>Customer review score</span>
            <strong>30%</strong>
          </div>

          <div className="trust-formula-result">
            <ShieldCheck size={24} />
            Final GutTrust Score
          </div>
        </div>
      </section>

      <section className="trust-principles-card">
        <div>
          <ShieldCheck size={28} />
        </div>

        <div>
          <h2>
            What our trust system prioritises
          </h2>

          <p>
            Verified participation, transparent
            scoring, accountable inspections and
            feedback connected to real delivered
            orders.
          </p>
        </div>
      </section>
    </div>
  );
}

export default TrustProcessPage;