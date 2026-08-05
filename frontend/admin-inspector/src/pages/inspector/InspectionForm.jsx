import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";



import {
    getInspectionById,
    getAllTests,
    getInspectionTestResults,
    startInspection,
    submitInspection,
    saveTestResult,
    getApiErrorMessage,
} from "../../services/inspectorService";

import TestResultModal from "../../pages/inspector/TestResultModal";
import { formatDate } from "../../utils/dateFormatter";



export default function InspectionForm() {
    const [selectedTest, setSelectedTest] = useState(null);
    const [modalOpen, setModalOpen] = useState(false);
    const { inspectionId } = useParams();

    const [inspection, setInspection] = useState(null);
    const [tests, setTests] = useState([]);
    const [completedTests, setCompletedTests] = useState([]);

    const [remarks, setRemarks] = useState("");
    const [recommendation, setRecommendation] = useState("");

    useEffect(() => {

        loadInspection();
        loadTests();
        loadCompletedTests();

    }, []);

    async function loadInspection() {

        try {

            const data = await getInspectionById(inspectionId);

            setInspection(data);

        } catch (error) {

            console.log(error);

        }

    }

    async function loadTests() {

        try {

            const data = await getAllTests();

            setTests(data);

        } catch (error) {

            console.log(error);

        }

    }

    async function loadCompletedTests() {

        try {

            const data = await getInspectionTestResults(inspectionId);

            setCompletedTests(data);
            

        } catch (error) {

            console.log(error);

        }

    }

    async function handleStartInspection() {

        try {

            await startInspection(inspectionId);

            alert("Inspection Started");

            loadInspection();

        } catch (error) {

            console.log(error);

        }

    }

    async function submitInspectionReport() {

        try {

            await submitInspection(
                inspectionId,
                remarks,
                recommendation
            );

            alert("Inspection Submitted");

            loadInspection();

        } catch (error) {
    alert(getApiErrorMessage(error, "Failed to submit inspection report"));
}

    }

    async function handleSaveTest(form) {

    try {

        await saveTestResult(inspectionId, form);

        alert("Test Saved Successfully");

        setModalOpen(false);

        setSelectedTest(null);

        await loadCompletedTests();

    } catch (error) {
    alert(getApiErrorMessage(error, "Failed to save test result"));
}
}

    if (!inspection) {

        return <h2 className="p-6">Loading...</h2>;

    }
    const completedIds = completedTests.map(result => result.testId);

    return (
        

        <div className="p-6 space-y-8">

           

            <h1 className="text-3xl font-bold">
                Inspection Report
            </h1>

            {/* Shop Information */}

            <div className="bg-white shadow rounded-lg p-6">

                <h2 className="text-xl font-bold mb-4">
                    Shop Information
                </h2>

                <p><b>Shop :</b> {inspection.shopName}</p>

                <p><b>Vendor :</b> {inspection.vendorName}</p>

                <p><b>Date :</b> {formatDate(inspection.inspectionDate)}</p>

                <p><b>Status :</b> {inspection.status}</p>

            </div>

            {/* Start Button */}

            {

                inspection.status === "ASSIGNED" &&

                <button

                    onClick={handleStartInspection}

                    className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded"

                >

                    Start Inspection

                </button>

            }

            {/* Available Tests */}

            <div className="bg-white shadow rounded-lg p-6">

                <h2 className="text-xl font-bold mb-5">

                    Food Safety Tests

                </h2>

                {
                    inspection.status === "IN_PROGRESS" &&
                    tests.map(test => (

                        <div

                            key={test.testId}

                            className="border rounded-lg p-5 mb-4"

                        >
                       

                            <h3 className="text-lg font-bold">

                                {test.productName}

                            </h3>

                            <p>

                                <b>Test :</b>

                                {test.testTitle}

                            </p>

                            <p>

                                <b>Adulterant :</b>

                                {test.adulterantName}

                            </p>
<button
    disabled={completedIds.includes(test.testId)}
    onClick={() => {
        setSelectedTest(test);
        setModalOpen(true);
    }}
    className={`mt-3 px-4 py-2 rounded text-white ${
        completedIds.includes(test.testId)
            ? "bg-gray-400 cursor-not-allowed"
            : "bg-green-600 hover:bg-green-700"
    }`}
>
    {completedIds.includes(test.testId)
        ? "Completed"
        : "Perform Test"}
</button>

                        </div>

                    ))

                }

            </div>

            {/* Completed Tests */}

            <div className="bg-white shadow rounded-lg p-6">

                <h2 className="text-xl font-bold mb-5">

                    Completed Tests

                </h2>

                {

                    completedTests.length === 0 ?

                        <p>No Test Performed Yet.</p>

                        :

                        completedTests.map(result => (

                            <div

                                key={result.resultId}

                                className="bg-green-50 border rounded-lg p-5 mb-4"

                            >

                                <h3 className="font-bold">

                                    {result.productName}

                                </h3>

                                <p>{result.testTitle}</p>

                                <p>

                                    Outcome :

                                    {result.outcome}

                                </p>

                                <p>

                                    Score :

                                    {result.scoreAwarded}

                                </p>

                            </div>

                        ))

                }

            </div>

            {/* Final Report */} 
            {
inspection.status === "IN_PROGRESS" && (

            <div className="bg-white shadow rounded-lg p-6 space-y-5">

                <h2 className="text-xl font-bold">

                    Final Inspection Report

                </h2>

                <textarea

                    rows={6}

                    className="border rounded w-full p-3"

                    placeholder="Inspector Remarks"

                    value={remarks}

                    onChange={(e) => setRemarks(e.target.value)}

                />

                <select

                    className="border rounded w-full p-3"

                    value={recommendation}

                    onChange={(e) =>

                        setRecommendation(e.target.value)

                    }

                >

                    <option value="">

                        Select Recommendation

                    </option>

                    <option value="APPROVED">

                        APPROVE

                    </option>

                    <option value="REJECTED">

                        REJECT

                    </option>

                    <option value="RE_INSPECTION_REQUIRED">

                        REINSPECTION

                    </option>
                      <option value="LAB_CONFIRMATION_REQUIRED">

                        LAB CONFIRMATION REQUIRED

                    </option>

                </select>

                <TestResultModal

    open={modalOpen}

    onClose={() => setModalOpen(false)}

    test={selectedTest}

    onSave={handleSaveTest}

/>

               <button
    disabled={completedTests.length === 0}
    onClick={submitInspectionReport}

                    className={`px-6 py-3 rounded text-white ${
    completedTests.length === 0
        ? "bg-gray-400 cursor-not-allowed"
        : "bg-green-700 hover:bg-green-800"
}`}

                >

                    Submit Inspection Report

                </button>

            </div>
        )}

        </div>

    );

}